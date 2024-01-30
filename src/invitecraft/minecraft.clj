(ns invitecraft.minecraft
  (:require [dotenv :as env])
  (:import nl.vv32.rcon.Rcon))

(def ^:private hostname (delay (or (env/env "RCON_HOSTNAME") "127.0.0.1")))
(def ^:private port (delay (Integer/parseInt (or (env/env "RCON_PORT") "25575"))))
(def ^:private password (delay (env/env "RCON_PASSWORD")))

(defn open-rcon-client []
  (let [rcon-client (Rcon/open @hostname @port)]
    (or (.authenticate rcon-client @password)
        (throw (Exception. "RCON-Authentication failed!")))
    rcon-client))

(defn run [cmd]
  (with-open [rcon-client (open-rcon-client)]
    (.sendCommand rcon-client cmd)))

(defn whitelist [player]
  (run (str "whitelist add " player)))
