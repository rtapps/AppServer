package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import db.mongo.MongoConnection;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		DB db = MongoConnection.get().getMongoClient().getDB("rtapps");
		DBCollection table = db.getCollection("adminusers");
		DBCursor curs = table.find();
		
		while(curs.hasNext()) {
            DBObject o = curs.next();
            String fname = (String) o.get("name") ; 
            String bname = (String) o.get("business_name") ; 
            String aid = (String) o.get("app_id") ; 
            String un = (String) o.get("username") ;
            String pd = (String) o.get("password") ;
    		response.getWriter().append(fname +", " + bname +", " + aid + ", " + un + ", " + pd);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
