(defproject checkers "0.0.1-SNAPSHOT"
  :description "FIXME: write description"

  :dependencies [[com.badlogicgames.gdx/gdx "1.1.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.1.0"]
                 [com.badlogicgames.gdx/gdx-box2d "1.1.0"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.1.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.1.0"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.1.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.1.0"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.6.0"]
                 [play-clj "0.3.8"]
                 [net.mikera/core.matrix "0.23.0"]
                 [clatrix "0.3.0"]
                 [speclj "3.0.2"]
                 [speclj-notify-osd "0.0.2"]]

   :test-paths ["spec"]
   :plugins [[speclj "3.0.2"]]

  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [checkers.core.desktop-launcher]
  :main checkers.core.desktop-launcher)
