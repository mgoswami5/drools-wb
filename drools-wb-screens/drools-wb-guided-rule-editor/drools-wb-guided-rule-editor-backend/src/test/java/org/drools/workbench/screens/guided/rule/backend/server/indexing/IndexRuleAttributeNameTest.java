/*
 * Copyright 2014 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.rule.backend.server.indexing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.drools.workbench.screens.guided.rule.type.GuidedRuleDRLResourceTypeDefinition;
import org.junit.Test;
import org.kie.workbench.common.services.refactoring.backend.server.BaseIndexingTest;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.RuleAttributeNameAnalyzer;
import org.kie.workbench.common.services.refactoring.backend.server.query.builder.BasicQueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleAttributeIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueRuleAttributeIndexTerm;
import org.uberfire.ext.metadata.backend.lucene.index.LuceneIndex;
import org.uberfire.ext.metadata.engine.Index;
import org.uberfire.ext.metadata.io.KObjectUtil;
import org.uberfire.java.nio.file.Path;

import static org.apache.lucene.util.Version.*;
import static org.junit.Assert.*;

public class IndexRuleAttributeNameTest extends BaseIndexingTest<GuidedRuleDRLResourceTypeDefinition> {

    @Test
    public void testIndexDrlRuleAttributeNames() throws IOException, InterruptedException {
        //Add test files
        final Path path = basePath.resolve( "drl1.rdrl" );
        final String drl = loadText( "drl1.rdrl" );
        ioService().write( path,
                           drl );

        Thread.sleep( 5000 ); //wait for events to be consumed from jgit -> (notify changes -> watcher -> index) -> lucene index

        final Index index = getConfig().getIndexManager().get( KObjectUtil.toKCluster( basePath.getFileSystem() ) );

        {
            final Query query = new BasicQueryBuilder()
                    .addTerm( new ValueRuleAttributeIndexTerm( "ruleflow-group" ) )
                    .build();
            searchFor(index, query, 1 );
        }
    }

    @Override
    protected TestIndexer getIndexer() {
        return new TestGuidedRuleDrlFileIndexer();
    }

    @Override
    public Map<String, Analyzer> getAnalyzers() {
        return new HashMap<String, Analyzer>() {{
            put( RuleAttributeIndexTerm.TERM,
                 new RuleAttributeNameAnalyzer( ) );
        }};
    }

    @Override
    protected GuidedRuleDRLResourceTypeDefinition getResourceTypeDefinition() {
        return new GuidedRuleDRLResourceTypeDefinition();
    }

    @Override
    protected String getRepositoryName() {
        return this.getClass().getSimpleName();
    }

}
