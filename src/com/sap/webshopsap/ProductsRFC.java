package com.sap.webshopsap;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

/**
 * Servlet implementation class ProductsRFC
 */
@WebServlet("/ProductsRFC")
public class ProductsRFC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductsRFC() {
        super();
        // TODO Auto-generated constructor stub
    }

    JSONObject prodJson = new JSONObject();
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
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
            JCoFunction stfcConnection=repo.getFunction("STFC_CONNECTION");        
            
            
            // Write data to table of import parameter
            JCoParameterList myField = stfcConnection.getImportParameterList();
            //myTable.appendRow();
            //myTable.setValue("JO", "Wasgeht");
            
          //Execute the RFC via the SAP CP Destination
            stfcConnection.execute(destination);
            
            // Read data from Function module table
            
            JSONObject prodElemJson = new JSONObject();

            /*for (int i = 0; i < myTable.getNumRows(); i++, myTable.nextRow()) 
            { 
            	myTable.setRow(i);
            	prodElemJson.put("TITLE from DB", myTable.getRow());        	
            }*/
            
            
            response.addHeader("Content-type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            responseWriter.write(prodElemJson.toString());         
            
            // Products Object 
            /*JSONObject prodElemListJson = new JSONObject();
            prodJson.put("products", prodElemListJson);
            
            JSONObject prodElemJson = new JSONObject();
            prodElemJson.put("category", import_result);
            prodElemJson.put("imageUrl", "");
            prodElemJson.put("price", "");
            prodElemJson.put("title", "");
            
            prodElemListJson.put("ID", prodElemJson);
                      
            // Form the JSON object
            response.setCharacterEncoding("UTF-8");
                   
            // LINE NO 57 
            response.addHeader("Content-type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            
            // Set the response as the JSON STRING
            responseWriter.write(prodJson.toString());*/
			} catch (AbapException ae) {
			ae.printStackTrace();
        } catch (JCoException e) {
        	e.printStackTrace();
        	
		} catch (JSONException e) {			
			e.printStackTrace();
		} 
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
