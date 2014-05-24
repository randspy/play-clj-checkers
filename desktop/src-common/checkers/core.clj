(ns checkers.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [checkers.board :as board]))

(declare checkers main-screen)

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [background (texture "board/wood.jpg")
          boardfield (board/generate-board)]
      [background boardfield]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
     (= (:key screen) (key-code :r)) (on-gl (set-screen! checkers main-screen))
     :else entities)))

(defgame checkers
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

(-> main-screen :entities deref)
