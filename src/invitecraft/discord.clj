(ns invitecraft.discord
  (:require [dotenv :as env]
            [clojure.core.async :as async]
            [discljord.connections :as dconnections]
            [discljord.messaging :as dmessaging]
            [invitecraft.minecraft :as minecraft]
            [clojure.string :as cstr]))

(def ^:private token (delay (env/env "BOT_TOKEN")))
(def ^:private whitelist-channel (delay (env/env "WHITELIST_CHANNEL")))

(def ^:private echannel (atom nil))
(def ^:private cchannel (atom nil))
(def ^:private mchannel (atom nil))

(defn prepare-bot []
  (reset! echannel (async/chan 100))
  (reset! cchannel (dconnections/connect-bot! @token @echannel :intents #{:guild-messages}))
  (reset! mchannel (dmessaging/start-connection! @token)))

(defn stop-bot []
  (dmessaging/stop-connection! @mchannel)
  (dconnections/disconnect-bot! @cchannel)
  (async/close! @echannel))

(defn handle-message-event [data]
  (when (= (:channel-id data) @whitelist-channel)
    (-> data
        :content
        cstr/trim-newline
        (cstr/replace #" " "")
        minecraft/whitelist)))

(defn start-bot []
  (prepare-bot)

  (try
    (loop []
      (let [[event-type event-data] (async/<!! @echannel)]
        (when (= event-type :message-create)
          (handle-message-event event-data))
        (recur)))
    (finally
      (stop-bot))))
