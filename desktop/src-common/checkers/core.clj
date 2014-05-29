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
          boardfield (board/gen-board board/board)
          new-game-pawns (board/gen-board board/new-game)]
      [background boardfield new-game-pawns]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
     (= (:key screen) (key-code :r)) (on-gl (set-screen! checkers main-screen))
     :else entities))

  :on-touch-down
  (fn [screen entities]
    (let [pos (input->screen screen (:input-x screen) (:input-y screen))]
      (do (print (board/filter-location (board/filter-pawns entities) pos))
        entities))))

(defn replace-board-elem [position old-elem new-elem])
(defgame checkers
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

(-> main-screen :entities deref)
