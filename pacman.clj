(use '[clojure.contrib.seq-utils :only (positions)])

(def nav {\. 1 \* 10 \B 'respawn \! 'respawn \# nil \space nil})

(defn read-in
  [file-path]
  (with-open [rdr (clojure.java.io/reader file-path)]
    (reduce
     (fn [acc line] (conj acc (vec (seq line))))
     [] (line-seq rdr))))

(defn write-out
  [file-path field]
  (with-open [wtr (clojure.java.io/writer file-path)]
    (doseq [item field]
        (.write wtr (str (apply str item) "\r\n")))))

(defn get-pos
  [s field]
  (let [row (first (positions #{s} (map #(some #{s} %) field)))
        col (first (positions #{s} (field row)))]
    [row col]))

(defn neighbours
  [[row col]]
  (seq [[row (inc col)] [(inc row) col] [row (dec col)] [(dec row) col]]))




(def field (read-in "initial.state"))
(def me (get-pos \A field))
(def n (neighbours me))
(def nv (zipmap n (map #(nav (get-in field %)) n)))
(def nv1 (remove #(let [[[_ _] x] %] (= x nil)) (seq nv)))

;;test write-out
(def nextfield (assoc-in (assoc-in field [10 4] \A) me \space))
(write-out "next.state" nextfield)