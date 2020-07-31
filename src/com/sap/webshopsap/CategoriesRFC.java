package com.sap.webshopsap;

import java.io.IOException;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Servlet implementation class RFCConnectivity
 */
@WebServlet("/CategoriesRFC")
public class CategoriesRFC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesRFC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter responseWriter = response.getWriter();		
		try
		{
			// LINE NO 38
            // Access the SAP Cloud Platform Destination "Q7W_RFC"
            JCoDestination destination=JCoDestinationManager.getDestination("RFCConnection");
            
            // Make an invocation of STFC_CONNECTION in the backend
            JCoRepository repo=destination.getRepository();
            JCoFunction stfcConnection=repo.getFunction("ZT_CATEGORIES");
            
            //Execute the RFC via the SAP CP Destination
            stfcConnection.execute(destination);

            
            // Get the exporting parameters ECHOTEXT and RESPTEXT of the RFC
            JCoParameterList exports=stfcConnection.getExportParameterList();
            String c_result=exports.getString("C_RESULT");
            String c_bread=exports.getString("C_BREAD");
            String c_dairy=exports.getString("C_DAIRY");
            String c_fruits=exports.getString("C_FRUITS");
            String c_seasonings=exports.getString("C_SEASONINGS");
            String c_veggies=exports.getString("C_VEGGIES");

            // Categories Object 
            JSONObject catJson = new JSONObject();
            JSONObject catElemJson = new JSONObject();
            catJson.put("categories", catElemJson);

            
            // Add CatElementsObj to Categories Json
            catElemJson.put("bread", c_bread);
            catElemJson.put("dairy", c_dairy);
            catElemJson.put("fruits", c_fruits);
            catElemJson.put("seasonings", c_seasonings);
            catElemJson.put("vegetables", c_veggies);
                      
            // Form the JSON object
            //response.setCharacterEncoding("UTF-8");
                   
            // LINE NO 57 
            response.addHeader("Content-type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            
            // Set the response as the JSON STRING
            responseWriter.write(catJson.toString());
		} catch (AbapException ae) {
			ae.printStackTrace();
        } catch (JCoException e) {
        	e.printStackTrace();
        	
		} catch (JSONException e) {			
			e.printStackTrace();
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
