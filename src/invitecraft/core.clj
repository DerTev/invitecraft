(ns invitecraft.core
  (:require [dotenv :as env]
            [clojure.core.server :as ccserver]
            [invitecraft.discord :as discord])
  (:gen-class))

(defn -main [& args]
  (let [repl-port (env/env "REPL_PORT")]
    (when (some? repl-port)
      (try
        (println (str "Starting Socket REPL on port " repl-port "..."))
        (ccserver/start-server {:port (Integer/parseInt repl-port)
                                :name "Remote Dev"
                                :accept 'clojure.core.server/repl})
        (catch Exception exception
          (println (str "Error while starting Socket REPL: " (.getMessage exception)))))))

  (println "Starting Discord Bot...")
  (discord/start-bot))
