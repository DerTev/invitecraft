(defproject invitecraft "0.1.0"
  :description "Let your friends whitelist themselves via Discord."
  :url "https://dertev.de/invitecraft/"
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [lynxeyes/dotenv "1.1.0"]
                 [nl.vv32.rcon/rcon "1.2.0"]
                 [com.github.discljord/discljord "1.3.1"]]
  :main ^:skip-aot invitecraft.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
