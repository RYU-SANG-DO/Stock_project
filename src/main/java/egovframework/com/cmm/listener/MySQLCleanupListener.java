package egovframework.com.cmm.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class MySQLCleanupListener implements ServletContextListener  {

	 @Override
	    public void contextInitialized(ServletContextEvent sce) {
	        System.out.println("웹 애플리케이션 시작됨.");
	    }

	    @Override
	    public void contextDestroyed(ServletContextEvent sce) {
	        System.out.println("웹 애플리케이션 종료됨. MySQL 리소스 정리 중...");

	        try {
	            // MySQL Cleanup 쓰레드 종료
	            AbandonedConnectionCleanupThread.checkedShutdown();
	            
	            // JDBC 드라이버 강제 해제
	            Enumeration<Driver> drivers = DriverManager.getDrivers();
	            while (drivers.hasMoreElements()) {
	                Driver driver = drivers.nextElement();
	                try {
	                    DriverManager.deregisterDriver(driver);
	                    System.out.println("JDBC 드라이버 해제됨: " + driver);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        System.out.println("MySQL 리소스 정리 완료.");
	    }

}
