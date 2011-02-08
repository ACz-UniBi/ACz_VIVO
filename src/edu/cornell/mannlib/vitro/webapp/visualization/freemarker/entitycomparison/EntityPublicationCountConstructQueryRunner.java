/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.visualization.freemarker.entitycomparison;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.iri.IRI;
import com.hp.hpl.jena.iri.IRIFactory;
import com.hp.hpl.jena.iri.Violation;
import com.hp.hpl.jena.query.DataSource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.cornell.mannlib.vitro.webapp.visualization.constants.QueryConstants;
import edu.cornell.mannlib.vitro.webapp.visualization.exceptions.MalformedQueryParametersException;

public class EntityPublicationCountConstructQueryRunner {
	
	protected static final Syntax SYNTAX = Syntax.syntaxARQ;
	
	private String egoURI;
	
	private DataSource dataSource;
	
	private Log log = LogFactory.getLog(EntityPublicationCountConstructQueryRunner.class.getName());
	
	public EntityPublicationCountConstructQueryRunner(String egoURI, DataSource dataSource, Log log){
		this.egoURI = egoURI;
		this.dataSource = dataSource;
		//this.log = log;		
	}
	
	private String generateConstructQueryForOrganizationLabel(String queryURI) {
		
		String sparqlQuery = 
			 "CONSTRUCT { " 
			+	"<"+queryURI+ ">  rdfs:label ?organizationLabel ."
			+ "}"	
			+ "WHERE {"
			+	"<"+queryURI+ ">  rdfs:label ?organizationLabel "
			+ "}";
				
		return sparqlQuery;
	}
	
	private String generateConstructQueryForSubOrganizations(String queryURI){
		
		String sparqlQuery = 			 
		
			"CONSTRUCT { "
			+	"<"+queryURI+ "> core:hasSubOrganization ?subOrganization . "
			+	"?subOrganization rdfs:label ?subOrganizationLabel . "
			+ 	"?subOrganization core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Person rdfs:label ?PersonLabel . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document rdfs:label ?DocumentLabel "
			+"}"
			+ "WHERE { "
			+	"<"+queryURI+ "> core:hasSubOrganization ?subOrganization . "
			+	"?subOrganization rdfs:label ?subOrganizationLabel . "
			+ 	"?subOrganization core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Person rdfs:label ?PersonLabel . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document rdfs:label ?DocumentLabel "		
			+ "}" ;
					
		
		return sparqlQuery;
	
	}

	private String generateConstructQueryForPersons(String queryURI){
		
		String sparqlQuery = 			 
		
			"CONSTRUCT { "
			+	"<"+queryURI+ "> core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Person rdfs:label ?PersonLabel . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document rdfs:label ?DocumentLabel "
			+"}"
			+ "WHERE { "
			+	"<"+queryURI+ "> core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Person rdfs:label ?PersonLabel . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document rdfs:label ?DocumentLabel "		
			+ "}" ;
					
		
		return sparqlQuery;
	
	}
	
	private String generateConstructQueryForDocumentDateTimeValueOneLevelDeep(String queryURI){
		
		String sparqlQuery = 			 
		
			"CONSTRUCT { "
			+	"<"+queryURI+ "> core:hasSubOrganization ?subOrganization . "
			+ 	"?subOrganization core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document core:dateTimeValue ?dateTimeValue . "
			+	"?dateTimeValue core:dateTime ?publicationDate . "
			+	"?Document core:year ?publicationYearUsing_1_1_property "
			+"}"
			+ "WHERE { "
			+	"<"+queryURI+ "> core:hasSubOrganization ?subOrganization . "
			+ 	"?subOrganization core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"{"
				+	"?Document core:dateTimeValue ?dateTimeValue . "
				+	"?dateTimeValue core:dateTime ?publicationDate "
			+	"}"
			+	"UNION "
			+	"{"
				+	"?Document core:year ?publicationYearUsing_1_1_property "
			+	"}"
			+ "}" ;
					
		
		return sparqlQuery;
	
	}	
	
	private String generateConstructQueryForDocumentDateTimeValue(String queryURI){
		
		String sparqlQuery = 			 
		
			"CONSTRUCT { "
			+	"<"+queryURI+ "> core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"?Document core:dateTimeValue ?dateTimeValue . "
			+	"?dateTimeValue core:dateTime ?publicationDate . "
			+	"?Document core:year ?publicationYearUsing_1_1_property "
			+"}"
			+ "WHERE { "
			+	"<"+queryURI+ "> core:organizationForPosition ?Position . "
			+   "?Position core:positionForPerson ?Person . "
			+	"?Person  core:authorInAuthorship ?Resource . "
			+	"?Resource core:linkedInformationResource ?Document . " 
			+	"?Document rdf:type bibo:Document . "
			+	"{"
				+	"?Document core:dateTimeValue ?dateTimeValue . "
				+	"?dateTimeValue core:dateTime ?publicationDate "
			+	"}"
			+	"UNION "
			+	"{"
				+	"?Document core:year ?publicationYearUsing_1_1_property "
			+	"}"
			+ "}" ;
					
		
		return sparqlQuery;
	
	}		
	
	private Model executeQuery(Set<String> constructQueries, DataSource dataSource) {
		
        Model constructedModel = ModelFactory.createDefaultModel();

        for (String queryString : constructQueries) {
            
        	log.debug("CONSTRUCT query string : " + queryString);
            
        	Query query = null;
        	
        	try{
        		query = QueryFactory.create(QueryConstants.getSparqlPrefixQuery() + queryString, SYNTAX);
        		//log.info("query: "+ queryString);
        	}catch(Throwable th){
                log.error("Could not create CONSTRUCT SPARQL query for query " +
                        "string. " + th.getMessage());
                log.error(queryString);
        	}
        	
            QueryExecution qe = QueryExecutionFactory.create(
                    query, dataSource);
            try {
                qe.execConstruct(constructedModel);
            } finally {
                qe.close();
            }
        	
        }	
       // constructedModel.write(System.out);
        return constructedModel;
	}	
	
	public Model getConstructedModel()
	throws MalformedQueryParametersException {

	if (StringUtils.isNotBlank(this.egoURI)) {
		/*
    	 * To test for the validity of the URI submitted.
    	 * */
    	IRIFactory iRIFactory = IRIFactory.jenaImplementation();
		IRI iri = iRIFactory.create(this.egoURI);
        if (iri.hasViolation(false)) {
            String errorMsg = ((Violation) iri.violations(false).next()).getShortMessage();
            log.error("Entity Pub Count Construct Query " + errorMsg);
            throw new MalformedQueryParametersException(
            		"URI provided for an individual is malformed.");
        }
    } else {
        throw new MalformedQueryParametersException("URI parameter is either null or empty.");
    }
	
	Set<String> constructQueries = new LinkedHashSet<String>();
	
	populateConstructQueries(constructQueries);
	
	Model model	= executeQuery(constructQueries,
									   this.dataSource);
	
	return model;
		
	}

	private void populateConstructQueries(Set<String> constructQueries) {
		
		constructQueries.add(generateConstructQueryForOrganizationLabel(this.egoURI));
		constructQueries.add(generateConstructQueryForSubOrganizations(this.egoURI));
		constructQueries.add(generateConstructQueryForPersons(this.egoURI));
		constructQueries.add(generateConstructQueryForDocumentDateTimeValueOneLevelDeep(this.egoURI));
		constructQueries.add(generateConstructQueryForDocumentDateTimeValue(this.egoURI));
		
	}	
}
