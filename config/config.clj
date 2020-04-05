{
 :db {:connection-timeout 30000
      :idle-timeout 600000
      :max-lifetime 1800000
      :minimum-idle 10
      :maximum-pool-size  10
      :adapter "mysql"
      :username "username"
      :password "password"
      :database-name "datbase-name?useUnicode=yes&characterEncoding=UTF-8"
      :server-name "127.0.0.1"
      :port-number 3306}
 :db-dev {
          :subprotocol "mysql"
          :subname "//localhost/database-name?useUnicode=yes&characterEncoding=UTF-8"
          :user "username"
          :password "password"
          }
 :env "dev"
 :youtube-apikey "<Your Youtube API Key>"
 :youtube-url "https://www.googleapis.com/youtube/v3/search"
 :approver-email "Parent Email"
 :base-url "http://base-url of prod system"
 :smtp {
        :host "email-server"
        :port 587
        :user "email-id"
        :pass "password"
        :from "email-id"
        :threadpool {:corecount 10
                     :maxcount 20}
        :timeoutmillis 5000
        }
 :months [
          {:month 1
           :month-name "January"}
          {:month 2
           :month-name "February"}
          {:month 3
           :month-name "March"}
          {:month 4
           :month-name "April"}
          {:month 5
           :month-name "May"}
          {:month 6
           :month-name "June"}
          {:month 7
           :month-name "July"}
          {:month 8
           :month-name "August"}
          {:month 9
           :month-name "September"}
          {:month 10
           :month-name "October"}
          {:month 11
           :month-name "November"}
          {:month 12
           :month-name "December"}]
 :years [{:year 2018}
         {:year 2019}
         {:year 2020}]
 }
