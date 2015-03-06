(defproject temp-calc "0.1.0"

  :dependencies [
    [org.clojure/clojure "1.6.0"]
    [org.clojure/clojurescript "0.0-2913"]
    [cljsjs/jquery "1.9.0-0"]
    [cljsjs/react "0.12.2-5"]
    [quiescent "0.1.4"]
    [sablono "0.3.1"]]

  :plugins [[lein-cljsbuild "1.0.5"]]

  :source-paths ["src"]

  :clean-targets [
    "out"
    "public/js/app-dev.js"
    "public/js/app-release.js"]

  :cljsbuild {
    :builds {
      :dev {
        :source-paths ["src-cljs"]
        :compiler {
          :output-to "public/js/app-dev.js"
          :optimizations :whitespace }}

      :release {
        :source-paths ["src-cljs"]
        :compiler {
          :output-to "public/js/app-release.js"
          :optimizations :advanced
          :pretty-print false }}

}})
