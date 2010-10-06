/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pig.impl.eval.cond;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pig.data.Datum;
import org.apache.pig.impl.FunctionInstantiator;



public class AndCond extends Cond {
    private static final long serialVersionUID = 1L;
    public List<Cond> cList;
    
    public AndCond(List<Cond> cList) {
        this.cList = cList;
    }
    
    @Override
    public List<String> getFuncs() {
        List<String> funcs = new ArrayList<String>();
        for (Iterator<Cond> it = cList.iterator(); it.hasNext(); ) {
            funcs.addAll(it.next().getFuncs());
        }
        return funcs;
    }

    @Override
    public boolean eval(Datum input){
        for (Iterator<Cond> it = cList.iterator(); it.hasNext(); ) {
            if (it.next().eval(input) == false) return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Iterator<Cond> it = cList.iterator(); it.hasNext(); ) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(" AND ");
        }
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public void finish() {
        for (Cond c: cList)
            c.finish();
    }

    @Override
    public void instantiateFunc(FunctionInstantiator instantiaor)
            throws IOException {
        for (Cond c: cList)
            c.instantiateFunc(instantiaor);
        
    }
}
