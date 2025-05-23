/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.jwrmodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.jtool.prmodel.PullRequest;
import org.jtool.prmodel.DeficientPullRequest;

public class JsonFileWriter {
    
    private PullRequest pullRequest;
    private Str_PullRequest strPullRequest;
    
    private DeficientPullRequest deficientPullRequest;
    private Str_DeficientPullRequest strDeficientPullRequest;
    
    private  StringConverter strBuilder;
    
    private  File outputFile;
    
    public JsonFileWriter(PullRequest pullRequest, String outputFilePath) {
        this.pullRequest = pullRequest;
        this.outputFile = new File(outputFilePath);
        
        this.strBuilder = new StringConverter();
        this.strPullRequest = strBuilder.buildPullRequest(pullRequest);
    }
    
    public JsonFileWriter(DeficientPullRequest deficientPullRequest, String outputFilePath) {
        this.deficientPullRequest = deficientPullRequest;
        this.outputFile = new File(outputFilePath);
        
        this.strBuilder = new StringConverter();
        this.strDeficientPullRequest = strBuilder.buildDeficientPullRequest(deficientPullRequest);
    }
    
	public void writePRModel() {
		Gson gson = new Gson();

		try (JsonWriter writer = new JsonWriter(new FileWriter(outputFile, false))) {
			gson.toJson(strPullRequest, Str_PullRequest.class, writer);
			System.out.println("Succeeded to write PR " + pullRequest.getId() + " into a json file !");
		} catch (IOException e) {
			System.err.println("Could not write " + outputFile);
		}
	}
    
	public void writePRModelWithDataLoss() {
		Gson gson = new Gson();

		try (JsonWriter writer = new JsonWriter(new FileWriter(outputFile, false))) {
			gson.toJson(strDeficientPullRequest, Str_DeficientPullRequest.class, writer);
			System.out.println("Succeeded to write Deficient PR " + deficientPullRequest.getId() + " into a json file !");
		} catch (IOException e) {
			System.err.println("Could not write " + outputFile);
		}
	}
}
