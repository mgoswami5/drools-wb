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

package org.drools.workbench.screens.dtablexls.backend.server.indexing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.drools.workbench.screens.dtablexls.type.DecisionTableXLSResourceTypeDefinition;
import org.junit.Test;
import org.kie.workbench.common.services.refactoring.backend.server.BaseIndexingTest;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.RuleAttributeNameAnalyzer;
import org.kie.workbench.common.services.refactoring.backend.server.query.builder.BasicQueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleAttributeIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueTypeIndexTerm;
import org.uberfire.ext.metadata.engine.Index;
import org.uberfire.java.nio.file.Path;

public class IndexDecisionTableXLSMultipleTypesTest extends BaseIndexingTest<DecisionTableXLSResourceTypeDefinition> {

    @Test
    public void testIndexDecisionTableXLSMultipleTypes() throws IOException, InterruptedException {
        //Add test files
        final Path path1 = loadXLSFile( basePath,
                                        "dtable1.xls" );
        final Path path2 = loadXLSFile( basePath,
                                        "dtable2.xls" );

        Thread.sleep( 5000 ); //wait for events to be consumed from jgit -> (notify changes -> watcher -> index) -> lucene index

        final Index index = getConfig().getIndexManager().get( org.uberfire.ext.metadata.io.KObjectUtil.toKCluster( basePath.getFileSystem() ) );

        {
            final Query query = new BasicQueryBuilder()
                    .addTerm( new ValueTypeIndexTerm( "org.drools.workbench.screens.dtablexls.backend.server.indexing.classes.Applicant" ) )
                    .build();
            searchFor(index, query, 2, path1, path2);
        }

        {
            final Query query = new BasicQueryBuilder()
                    .addTerm( new ValueTypeIndexTerm( "org.drools.workbench.screens.dtablexls.backend.server.indexing.classes.Mortgage" ) )
                    .build();
            searchFor(index, query, 1, path2);
        }
    }

    @Override
    protected TestIndexer getIndexer() {
        return new TestDecisionTableXLSFileIndexer();
    }

    @Override
    public Map<String, Analyzer> getAnalyzers() {
        return new HashMap<String, Analyzer>() {{
            put( RuleAttributeIndexTerm.TERM,
                 new RuleAttributeNameAnalyzer() );
        }};
    }

    @Override
    protected DecisionTableXLSResourceTypeDefinition getResourceTypeDefinition() {
        return new DecisionTableXLSResourceTypeDefinition();
    }

    @Override
    protected String getRepositoryName() {
        return this.getClass().getSimpleName();
    }

    private Path loadXLSFile( final Path basePath,
                              final String fileName ) throws IOException {
        final Path path = basePath.resolve( fileName );
        final InputStream is = this.getClass().getResourceAsStream( fileName );
        final OutputStream os = ioService().newOutputStream( path );
        IOUtils.copy( is,
                      os );
        os.flush();
        os.close();
        return path;
    }

}
