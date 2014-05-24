(ns checkers.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [background (texture "board/wood.jpg")]
      [background]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities)))

(defgame checkers
  :on-create
  (fn [this]
    (set-screen! this main-screen)))
