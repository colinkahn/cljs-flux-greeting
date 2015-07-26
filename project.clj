(defproject greeting "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058" :scope "provided"]
                 [ring "1.2.1"]
                 [colinkahn.flux.getters "1.1.0"]
                 [colinkahn.flux.dispatcher "1.1.0"]
                 [reagent "0.5.0"]]
  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-ring "0.8.10"]]
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :cljsbuild { 
    :builds {
      :main {
        :source-paths ["src/cljs"]
        :compiler {:output-to "resources/public/js/cljs.js"
                   :optimizations :simple
                   :pretty-print true}
        :jar true}}}
  :main greeting.server
  :ring {:handler greeting.server/app})

