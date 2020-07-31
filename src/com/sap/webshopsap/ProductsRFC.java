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
            JCoFunction stfcConnection=repo.getFunction("ZT_CATEGORIES");
            
            // Set Importing Parameter REQUTEXT for the RFC
            JCoParameterList imports=stfcConnection.getImportParameterList();
            //imports.setValue("ID", "SAP HANA Cloud connectivity for SAP CP Workflow");
            imports.setValue("CATEGORY", "TEST");
            
            // WORKS!!
            JCoTable myTable = stfcConnection.getTableParameterList().getTable("PRODUCTS");
            myTable.appendRow();
            myTable.setValue("ID", 12);
            myTable.setValue("TITLE", "Jo");
            
         // Output of this servlet needs to be JSON if consumed from SAP CP Workflow
            JSONObject responseJson = new JSONObject();
            
            for (int i = 0; i < myTable.getNumRows(); i++, myTable.nextRow()) 
            { 
            	myTable.setRow(i);
            	responseJson.put("No of rows", myTable.getString("Jo"));        	
            }
            
            //Execute the RFC via the SAP CP Destination
            stfcConnection.execute(destination);
            
            // Get the exporting parameters ECHOTEXT and RESPTEXT of the RFC
            JCoParameterList exports=stfcConnection.getExportParameterList();
            //String echotext=exports.getString("ECHOID");
            String resptext=exports.getString("ECHOCAT");
           
            
            // Form the JSON object
            //responseJson.put("echoid", echotext);
            responseJson.put("echocat", resptext);
            // LINE NO 57 
            
            response.addHeader("Content-type", "application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            // Set the response as the JSON STRING
            responseWriter.write(responseJson.toString());
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
