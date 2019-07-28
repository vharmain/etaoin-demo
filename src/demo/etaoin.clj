(ns demo.etaoin
  (:require
   [clojure.test :refer [deftest use-fixtures]]
   [etaoin.api :as e]
   [etaoin.keys :as k])
  (:gen-class))

(def ^:dynamic *driver*)

(defn remote-driver []
  (e/create-driver :chrome-headless
                   {:host      "headless-chrome"
                    :port      4444
                    :log-level :info}))

;; (def remote-driver (remote-driver))
(def local-driver (e/chrome))

(comment

  ;; (e/connect-driver local-driver)
  (e/go local-driver "http://www.google.fi")
  (e/fill local-driver {:name "q"} "kissa")
  (e/get-title local-driver)
  (e/fill local-driver {:name "q"} k/enter)
  (e/click local-driver [{:id "search"}
                         {:tag :a :index 1}])
  (e/back local-driver)
  (e/forward local-driver)
  (e/get-url local-driver)

  (e/wait-visible local-driver [{:id :simpleSearch}
                                {:tag :input :name :search}])

  (e/scroll-down local-driver)
  (e/scroll-bottom local-driver)
  (e/scroll-top local-driver)

  (e/clear local-driver {:tag :input :name :search})
  (e/fill local-driver {:tag :input :name :search} "Clojure")
  (e/fill local-driver {:tag :input :name :search} k/enter)
  (e/wait-visible local-driver {:class :mw-search-results})

  (e/click local-driver [{:class :mw-search-results}
                 {:class :mw-search-result-heading}
                 {:tag :a}])
  (e/wait-visible local-driver {:id :firstHeading})
  (e/get-url local-driver)

  )

(defn run-test-1 [driver]
  (let [first-res [{:id "search"} {:tag :a :index 1}]]
    (doto driver
      (e/go "http://www.google.fi")
      (e/fill {:name "q"} "kissa")
      (e/get-title)
      (e/fill {:name "q"} k/enter)
      (e/wait-visible first-res)
      (e/click first-res))))

(run-test-1 local-driver)

(comment
  (some identity [nil nil 1])
  (->> [nil nil 1]
       (remove nil?)
       first)

  ;; (binding [*driver* local-driver])
  (e/doto-wait 1 *driver*
    (e/connect-driver *driver*)
    (e/go "http://proxy")
    (e/wait-visible {:id "account-btn"} {:timeout 60})
    (e/visible? {:id "account-btn"} )
    (e/click {:id "account-btn"})
    (e/click  {:id "account-menu-item-login"})
    (e/clear  {:id "login-username-input"})
    (e/clear  {:id "login-password-input"})
    (e/fill  {:id "login-username-input"} "admin")
    (e/fill  {:id "login-password-input"} "kissa13")
    (e/click  {:id "login-submit-btn"})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
