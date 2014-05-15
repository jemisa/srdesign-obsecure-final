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

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.metaintelligence.parsetree.XmlDocument;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
	return new XmlDocument(getDocumentContents());
    }
    
    public String getDocumentContents() throws IOException {
	return new String(Files.readAllBytes(getDocumentPath()));
    }
    
    private Path getDocumentPath() {
	return FileSystems.getDefault().getPath(getFullDocumentPathName(), StringUtils.EMPTY);
    }
    
    private String getFullDocumentPathName() {
	return "src/main/resources/meta.intelligence.xml";
    }
	    
}
