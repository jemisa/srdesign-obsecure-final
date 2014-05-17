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

package cs492.obsecurefinal.metaintelligence.parsetree;

import cs492.obsecurefinal.spring.domain.metaintelligence.MetaNode;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCategory;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCondition;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaCriteria;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaFilter;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaMetric;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRule;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaRuleSet;
import cs492.obsecurefinal.spring.domain.metaintelligence.MetaWeight;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Benjamin Arnold
 */
public enum XmlTag implements XmlConstants, Instantiable {
    RULE_SET {
	@Override
	public MetaRuleSet newInstance(RuleTreeComponent node) {
	    MetaRuleSet metaRuleSet = new MetaRuleSet();
	    metaRuleSet.setName(node.getName());
	    return metaRuleSet;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    //There is no parent to attach the MetaRuleSet to
	}
    },CATEGORY{
	@Override
	public MetaCategory newInstance(RuleTreeComponent node) {
	    MetaCategory metaCategory = new MetaCategory();
	    metaCategory.setName(node.getName());
	    return metaCategory;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaRuleSet)parent).addMetaCatagory((MetaCategory)child);
	}
    },METRIC{
	@Override
	public MetaMetric newInstance(RuleTreeComponent node) {
	    return new MetaMetric();
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaRule)parent).setMetric((MetaMetric)child);
	}
    },WEIGHT{
	@Override
	public MetaWeight newInstance(RuleTreeComponent node) {
	    MetaWeight metaWeight = new MetaWeight();
	    metaWeight.setName(node.getName());
	    if (NumberUtils.isNumber(node.getValue())) {
		 metaWeight.setValue(Integer.parseInt(node.getValue()));
	    }	   
	    return metaWeight;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaMetric)parent).addWeight((MetaWeight) child);
	}
    },FILTER{
	@Override
	public MetaFilter newInstance(RuleTreeComponent node) {
	    MetaFilter metaFilter = new MetaFilter();
	    metaFilter.setName(node.getName());
	    metaFilter.setType(node.getType());
	    if (NumberUtils.isNumber(node.getValue())) {
		metaFilter.setValue(Integer.parseInt(node.getValue()));
	    }
	    return metaFilter;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaRule)parent).setFilter((MetaFilter) child);
	}
    },RULE{
	@Override
	public MetaRule newInstance(RuleTreeComponent node) {
	    MetaRule metaRule = new MetaRule();
	    metaRule.setName(node.getName());
	    metaRule.setType(node.getType());
	    return metaRule;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaCategory)parent).addMetaRule((MetaRule) child);
	}
    },OVERRIDE{
	@Override
	public MetaNode newInstance(RuleTreeComponent node) {
	    return null; //FIXME
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    //FIXME
	}
    },CRITERIA{
	@Override
	public MetaCriteria newInstance(RuleTreeComponent node) {
	    return new MetaCriteria();
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaRule)parent).setCriteria((MetaCriteria) child);
	}
    },CONDITION{
	@Override
	public MetaCondition newInstance(RuleTreeComponent node) {
	    MetaCondition metaCondition = new MetaCondition();
	    metaCondition.setName(node.getName());
	    metaCondition.setType(node.getType());
	    return metaCondition;
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    ((MetaCriteria)parent).addMetaCondition((MetaCondition) child);
	}
    },NOT_FOUND{
	@Override
	public MetaNode newInstance(RuleTreeComponent parent) {
	    return null; //FIXME
	}

	@Override
	void handleResult(MetaNode parent, MetaNode child) {
	    //FIXME
	}
    };
    
    private final String basis;
    private final String matchString;
    private final String matchEndString;
    
    private XmlTag(){
	String[] split = name().split("_");
	StringBuilder sb = new StringBuilder();
	for (String s : split) {
	    sb.append(StringUtils.capitalize(s.toLowerCase()));  //RULE_SET => ruleSet
	}
	basis = StringUtils.uncapitalize(sb.toString());
	matchString = String.format(TAG_OPEN_FORMAT,basis);
	matchEndString = String.format(TAG_END_FORMAT, basis);
    }
        
    public int location(String sample) {
	return sample.indexOf(basis);
    }
    
    public String getIdentifier() {
	return basis;
    }
    
    public RuleTreeComponent parse(String sample) {
	Pattern p0 = Pattern.compile(matchString);
	Pattern p1 = Pattern.compile(matchEndString);
	Matcher start = p0.matcher(sample);
	Matcher end = p1.matcher(sample);
	RuleTreeComponent node = (start.find() && end.find()) ? handleTag(sample, start,end) :  new RuleTreeLeaf(this); //sanity: should never hit this else
	return node;
    }

    private RuleTreeComponent handleTag(String sample, Matcher start, Matcher end) {
	String middleBit = sample.substring(start.end(),end.start());
	boolean hasChildren = middleBit.contains("<");
	RuleTreeComponent node = hasChildren ? handleChildren(middleBit) : new RuleTreeLeaf(this);
	if (!hasChildren && !middleBit.isEmpty()) {
	    node.setName(middleBit);
	}
	String startTag = start.group();
	String[] startSplit = startTag.split(" "); //FIXME: will break if we put spaces within quotes
	for (int i = 1; i < startSplit.length; i++) {  //<ruleSet name="strict" type="adaptive">
	    String[] args = startSplit[i].split("=");
	    String argName = args[0];
	    String argValue = args[1].replace("\"", StringUtils.EMPTY).replace(XML_TAG_CLOSE, StringUtils.EMPTY); 
	    node.setAppropriate(argName, argValue);
	}

	return node;
    }
    
    public RuleTreeComponent handleChildren(String middleBit) {
	RuleTreeComposite node = new RuleTreeComposite(this);
		
	String bit = middleBit;
	int indexStart = bit.indexOf(XML_TAG_OPEN);
	int indexEnd = bit.indexOf(XML_TAG_CLOSE)+1;
	
	while (indexStart > -1 && indexEnd > indexStart) {
	    String sTag = bit.substring(indexStart, indexEnd);
	    if (!(sTag.startsWith(XML_COMMENT_START) || sTag.startsWith(XML_DECLARATION_START) || sTag.startsWith(XML_TAG_END_START))) { 
		XmlTag tag = match(sTag.split(ARGUMENT_SEPARATOR)[0]);            //tag-name, ie:  [ruleSet, name="something", type="something"] 
		Pattern p0 = Pattern.compile(tag.matchString);
		Pattern p1 = Pattern.compile(tag.matchEndString);
		Matcher start = p0.matcher(bit);
		Matcher end = p1.matcher(bit);
		if (start.find() && end.find()) {
		    String lilBit = bit.substring(start.start(), end.end());
		    RuleTreeComponent child = tag.parse(lilBit);
		    node.addChild(child);
		    bit = bit.substring(end.end());
		}
	    } else {
		bit = bit.substring(indexEnd);
	    }
	    
	    indexStart = bit.indexOf(XML_TAG_OPEN);
	    indexEnd = bit.indexOf(XML_TAG_CLOSE)+1;
	}
	return node;
    }

    private XmlTag match(String sTag) {
	XmlTag[] tags = new XmlTag[]{RULE_SET,CATEGORY,METRIC,WEIGHT,FILTER,RULE,CRITERIA,CONDITION};
	int shortEnd = sTag.indexOf(XML_TAG_CLOSE);
	int open = sTag.indexOf(XML_TAG_OPEN) + 1;
	final String likelyTag = (shortEnd == -1) ? sTag.substring(open) : sTag.substring(open,shortEnd);
	for (XmlTag tag : tags) {
	    if (tag.getIdentifier().equals(likelyTag)) {
		return tag;
	    }
	}
	return NOT_FOUND;
    }

    abstract void handleResult(MetaNode parent, MetaNode child);
}
