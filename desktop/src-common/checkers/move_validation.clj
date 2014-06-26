(ns checkers.move-validation
  (:require [clojure.core.matrix :as mx]))


(defn- coordinates-positive? [coordinates]
  (not (some #(< % 0) coordinates)))

(defn- coordinates-smaller-than-dimensions? [dimensions coordinates]
  (not (some #(< % 1)
              (map - dimensions coordinates))))

(defn- inside-board? [{:keys [board from to]}]
  (and
   (coordinates-smaller-than-dimensions? (mx/shape board) from)
   (coordinates-smaller-than-dimensions? (mx/shape board) to)
   (coordinates-positive? from)
   (coordinates-positive? to)))

(defn- destination-field-empty? [{:keys [board to]}]
  (if (= 0 (mx/mget board (first to) (second to))) true false))

(defn- pawn-on-coordinates? [{:keys [board from]}]
  (not= 0 (mx/mget board (first from) (second from))))

(def white-pawn-validation-board (mx/matrix [[:beat     0        0     0 :beat]
                                             [    0 :move        0 :move     0]
                                             [    0     0 :white-p     0     0]
                                             [    0     0        0     0     0]
                                             [    0     0        0     0     0]]))

(def black-pawn-validation-board (mx/matrix [[    0     0        0     0     0]
                                             [    0     0        0     0     0]
                                             [    0     0 :black-p     0     0]
                                             [    0 :move        0 :move     0]
                                             [:beat     0        0     0 :beat]]))

(def white-queen-validation-board (mx/matrix [[:beat     0        0     0 :beat]
                                              [    0 :move        0 :move     0]
                                              [    0     0 :white-q     0     0]
                                              [    0 :move        0 :move     0]
                                              [:beat     0        0     0 :beat]]))

(def black-queen-validation-board (mx/matrix [[:beat     0        0     0 :beat]
                                              [    0 :move        0 :move     0]
                                              [    0     0 :black-q     0     0]
                                              [    0 :move        0 :move     0]
                                              [:beat     0        0     0 :beat]]))


(defn- get-pawn [board from]
  (mx/mget board (first from) (second from)))

(defn- get-valid-move-coordinates [board from]
  (let [pawn-type (get-pawn board from)]
    (case pawn-type
      :white-p white-pawn-validation-board
      :black-p black-pawn-validation-board
      :white-q white-queen-validation-board
      :black-q black-queen-validation-board)))

(defn- get-destination-from-validation-board [board from to]
  (let [x-math-vector (- (first to) (first from))
        y-math-vector (- (second to) (second from))
        validation-board (get-valid-move-coordinates board from)
        central-position-on-validation-board (int (/ (first (mx/shape validation-board)) 2))
        x-pawn-position (+ x-math-vector central-position-on-validation-board)
        y-pawn-position (+ y-math-vector central-position-on-validation-board)]
    (if (and (< x-pawn-position (first (mx/shape validation-board)))
             (< y-pawn-position (first (mx/shape validation-board))))
      (mx/mget validation-board
               x-pawn-position
               y-pawn-position)
      0)))

(defn- is-pawn-in-middle-enemy [pawn-in-middle pawn]
  (if (= pawn-in-middle 0) false
    (let [white-side [:white-p :white-q]
          is-pawn-on-white-side (some #(= pawn %) white-side)
          is-middle-pawn-on-white-side (some #(= pawn-in-middle %) white-side)]
      (not= is-pawn-on-white-side is-middle-pawn-on-white-side))))

(defn- pawn-allowed-to-move? [{:keys [board from to]}]
  (let [x-math-vector (- (first to) (first from))
        y-math-vector (- (second to) (second from))
        destination (get-destination-from-validation-board board from to)
        pawn-between-destination-and-original-pawn (mx/mget board
                                (+ (first from) (int (/ x-math-vector 2)))
                                (+ (second from) (int (/ y-math-vector 2))))]
     (or (= destination :move)
         (and (= destination :beat)
              (is-pawn-in-middle-enemy
                pawn-between-destination-and-original-pawn
                (get-pawn board from))))))

(defn- pawn-on-players-side? [{:keys [board from pawn-type]}]
  (some #(= (get-pawn board from) %) pawn-type))

(defn move-valid? [move]
  (and
    (inside-board? move)
    (pawn-on-coordinates? move)
    (pawn-allowed-to-move? move)
    (destination-field-empty? move)
    (pawn-on-players-side? move)))
