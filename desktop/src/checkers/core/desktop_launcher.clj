(ns checkers.core.desktop-launcher
  (:require [checkers.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. checkers "checkers" 800 533)
  (Keyboard/enableRepeatEvents true))

(-main)
