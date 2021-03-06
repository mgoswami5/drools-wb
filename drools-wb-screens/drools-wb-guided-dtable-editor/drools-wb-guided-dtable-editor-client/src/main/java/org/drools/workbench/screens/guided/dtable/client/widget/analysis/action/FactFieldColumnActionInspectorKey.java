/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.action;

import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;

public class FactFieldColumnActionInspectorKey
        extends ActionInspectorKey {

    private final String boundName;
    private final String factField;

    public FactFieldColumnActionInspectorKey( final ActionSetFieldCol52 actionCol ) {
        super( actionCol );
        this.boundName = actionCol.getBoundName();
        this.factField = actionCol.getFactField();
    }

    public FactFieldColumnActionInspectorKey( final ActionInsertFactCol52 actionCol ) {
        super( actionCol );
        this.boundName = actionCol.getBoundName();
        this.factField = actionCol.getFactField();
    }

    public String getBoundName() {
        return boundName;
    }

    public String getFactField() {
        return factField;
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        } else if ( o instanceof FactFieldColumnActionInspectorKey ) {
            FactFieldColumnActionInspectorKey other = (FactFieldColumnActionInspectorKey) o;
            return boundName.equals( other.boundName ) && factField.equals( other.factField );
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return boundName.hashCode() * 37 + factField.hashCode();
    }

    @Override
    public String toHumanReadableString() {
        return boundName + "." + factField;
    }

}
