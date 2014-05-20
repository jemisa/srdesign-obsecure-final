/*
 * Copyright (C) 2014 Benjamin Arnold
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cs492.obsecurefinal.metaintelligence;

import cs492.obsecurefinal.metaintelligence.parsetree.XmlDocument;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Benjamin Arnold
 */
public class XmlLoader { 
   
    public MetaRuleSet loadRules() throws IOException {
	XmlDocument document = getDocument();
	MetaRuleSet ruleSet = document.createRuleSet();
	return ruleSet;
    }
    
    public XmlDocument getDocument() throws IOException {
	String documentContents = null;
	try {
	    documentContents = getDocumentContents(getFullOverrideDocumentPathName());
	} catch (NoSuchFileException ex) {
	    //expected if not override
	}
	if (documentContents == null || documentContents.isEmpty()) {
	    documentContents = getDocumentContents(getFullDocumentPathName());
	}
	return new XmlDocument(documentContents);
    }
    
    public String getDocumentContents(String path) throws IOException {
	return new String(Files.readAllBytes(getDocumentPath(path)));
    }
    
    private Path getDocumentPath(String pathName) {
	return FileSystems.getDefault().getPath(pathName, StringUtils.EMPTY);
    }
    
    private String getFullDocumentPathName() {
	return "src/main/resources/meta.intelligence.xml";
    }
    
     private String getFullOverrideDocumentPathName() {
	String userHome = System.getProperty("user.home") ;
	return userHome + File.separator + "obsecure/meta.intelligence.xml";
    }
	    
}
