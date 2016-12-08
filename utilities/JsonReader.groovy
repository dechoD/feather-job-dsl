package utilities

import groovy.json.JsonSlurper

class JsonReader {
    static getJsonConfig(String fileName) {
        def inputFile = new File(fileName)

        //Resolves the json file when executed in the context of a Jenkins job
        if(!inputFile.exists()){
            def build = Thread.currentThread().executable
            inputFile = new File(build.workspace.toString() + "\\JobDslScripts\\" + fileName);
        }

        def jsonFile = new JsonSlurper().parseText(inputFile.text)
        return jsonFile;
    }
}