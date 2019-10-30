package com.rcm.app.dbConnector.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rcm.app.dbConnector.model.Connection;
import com.rcm.app.dbConnector.service.ConnectorService;
import com.rcm.app.dbConnector.util.Constants;
/**
 * Controller class handling all the requests from jsp's
 * 
 * All service and data layer exceptions to be cascaded to here and will eventually be sent back to error.jsp
 * 
 * @author rcm
 *
 */
@Controller
public class ConnectionController extends Constants {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);
	
	@Autowired
	private ConnectorService conService;
	
	/**
	 * endpoint to launch welcome page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showWelcomePage(ModelMap model) {
//		model.put("name", "Roopa");
		return "welcome";
	}
	
	/**
	 * end point to view user defined DB connections
	 * 
	 * @param model
	 * @return : list of all Connection objects from the DB
	 */
	@RequestMapping(value = "/list-cons", method = RequestMethod.GET)
	public String showConnections(ModelMap model) {
		LOGGER.info("showConnections :"+START);
		try {
			
			model.put("connections", conService.getAllConnections());

		} catch (Exception e) {
			LOGGER.error("showConnections :"+ERROR +" "+e.getMessage());
			return "error";
		}
		
		LOGGER.info("showConnections :"+END);
		return "list-connections";
	}
	
	/**
	 * Redirect to Connection creation page with empty model 
	 * 
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/add-connection", method = RequestMethod.GET)
	public String showAddConnection(ModelMap model) {
		LOGGER.info("showAddConnection :"+START);
		 
		Connection con = new Connection();
		try {
		
			model.addAttribute("connection", con);
			
		} catch (Exception e) {
			LOGGER.error("showAddConnection :"+ERROR +" "+e.getMessage());
			return "error";
		}    
		
	    LOGGER.info("showAddConnection :"+END);
	    return "connectionDeets";
	}
	
	/**
	 * Create a new connection Obj with jsp validated form params
	 * 
	 * @param model
	 * @param con
	 * @param result
	 * @return : save object and redirect to the page where user can view all Connections
	 */
	@RequestMapping(value = "/add-connection", method = RequestMethod.POST)
	public String addConnection(ModelMap model, @Valid Connection con, BindingResult result) {
		LOGGER.info("addConnection :"+START);
		if (result.hasErrors()) {
			LOGGER.error("addConnection :"+ERROR+" "+ result.getAllErrors().toString());
            return "con";
        }

		try {
			
			model.put("connection", new Connection());
			conService.addNewConnection(con);
			
		} catch (Exception e) {
			LOGGER.error("addConnection :"+ERROR +" "+e.getMessage());
			return "error";
		} 
		
		LOGGER.info("addConnection :"+END);
		return "redirect:/list-cons";
	}
	
	/**
	 * Redirect to page to enter credentials for single connection Detail to update
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update-connection", method = RequestMethod.GET)
    public String displayConnectionToUpdate(@RequestParam long id, ModelMap model) {
		LOGGER.info("displayConnectionToUpdate :"+START);
		
		try {
			
			Connection con = conService.getConById(id);
	        model.put("connection", con);
	        
		} catch (Exception e) {
			LOGGER.error("displayConnectionToUpdate :"+ERROR +" "+e.getMessage());
			return "error";
		} 
        
        LOGGER.info("displayConnectionToUpdate :"+END);
        return "updateConnection";
    }
	
	/**
	 * Update existing Connection obj with jsp validated form values
	 * 
	 * @param model
	 * @param con
	 * @param result
	 * @return : redirect to page that lists all connection obj
	 */
	@RequestMapping(value = "/update-connection", method = RequestMethod.POST)
    public String updateConnection(ModelMap model, @Valid Connection con, BindingResult result) {
		LOGGER.info("updateConnection :"+START);
		if (result.hasErrors()) {
			LOGGER.error("updateConnection :"+ERROR+" "+ result.getAllErrors().toString());
            return "con";
        }
		
		try {
			
			conService.updateConnection(con);
		
		} catch (Exception e) {
			LOGGER.error("updateConnection :"+ERROR +" "+e.getMessage());
			return "error";
		} 
		
		LOGGER.info("updateConnection :"+END);
        return "redirect:/list-cons";
    }
	
	/**
	 * Method to permanently remove a connection object
	 * 
	 * @param id : connection id that maps to PK of the entry in the DB
	 * @param model
	 * @return : redirect to page that displays all connections
	 */
	@RequestMapping(value = "/delete-connection", method = RequestMethod.GET)
    public String deleteConnection(@RequestParam long id, ModelMap model) {
		LOGGER.info("deleteConnection :"+START);
		
		try {
			
			conService.deleteConnection(id);
			
		} catch (Exception e) {
			LOGGER.error("deleteConnection :"+ERROR +" "+e.getMessage());
			return "error";
		} 
		
		LOGGER.info("deleteConnection :"+END);
        return "redirect:/list-cons";
    }
	
	/**
	 * Retrieve a particular connection object and send back all but password (entered by user) for further validation 
	 * 
	 * @param id : connection id that maps to PK of the entry in the DB
	 * @param model
	 * @return : Connection object
	 */
	@RequestMapping(value = "/conect-to-DB", method = RequestMethod.GET)
	public String listConnectionsForDetails(@RequestParam long id, ModelMap model) {
		LOGGER.info("listConnectionsForDetails :"+START);
		
		try {
			
			Connection con = conService.getConById(id);
	        model.put("connection", con);
			
		} catch (Exception e) {
			LOGGER.error("listConnectionsForDetails :"+ERROR +" "+e.getMessage());
			return "error";
		} 
        
        LOGGER.info("listConnectionsForDetails :"+END);
        return "validate-connection";
	}
	
	/**
	 * Method to build Metadata for a selected DB connection
	 * Metadata includes table names, column names & their data types
	 * Will also contain the table data
	 * 
	 * @param model
	 * @param con
	 * @param result
	 * @return : Custom object with Metadata for the DB and individual table data. Redirect to corresponding jsp page
	 */
	@RequestMapping(value = "/conect-to-DB", method = RequestMethod.POST)
	public String validateAndConnectTODb(ModelMap model, @Valid Connection con, BindingResult result) {
		LOGGER.info("validateAndConnectTODb :"+START);
		
		if (result.hasErrors()) {
			LOGGER.error("validateAndConnectTODb :"+ERROR+" "+ result.getAllErrors().toString());
            return "con";
        }
		
		try {
			if(conService.validateConnection(con)) {
				conService.fetchDBDetails(con);
				model.put("DBDetails", conService.fetchDBDetails(con));
				
				LOGGER.info("validateAndConnectTODb :"+END);
				return "display-db-details";
			} else {
				model.addAttribute("errors", "Failed to validate credentials");
				
				LOGGER.info("validateAndConnectTODb :"+END);
				return "validate-connection";
			}
		} catch (SQLException e) {
			LOGGER.error("validateAndConnectTODb :"+ERROR +" "+e.getMessage());
			model.addAttribute("errors", "Unable to connect to this DB. Check connection details");
			return "validate-connection";
		} catch (Exception e) {
			
			LOGGER.error("validateAndConnectTODb :"+ERROR +" "+e.getMessage());
			return "error";
			
		}
	}
}
