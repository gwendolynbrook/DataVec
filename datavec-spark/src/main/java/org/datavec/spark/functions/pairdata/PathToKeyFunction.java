/*-
 *  * Copyright 2016 Skymind, Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 */

package org.datavec.spark.functions.pairdata;

import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.input.PortableDataStream;
import scala.Tuple2;
import scala.Tuple3;

/** Given a Tuple2<String,PortableDataStream>, where the first value is the full path, map this
 * to a Tuple3<String,Integer,PortableDataStream> where the first value is a key (using a {@link PathToKeyConverter}),
 * second is an index, and third is the original data stream
 */
public class PathToKeyFunction implements
                PairFunction<Tuple2<String, PortableDataStream>, String, Tuple3<String, Integer, PortableDataStream>> {

    private PathToKeyConverter converter;
    private int index;

    public PathToKeyFunction(int index, PathToKeyConverter converter) {
        this.index = index;
        this.converter = converter;
    }

    @Override
    public Tuple2<String, Tuple3<String, Integer, PortableDataStream>> call(Tuple2<String, PortableDataStream> in)
                    throws Exception {
        Tuple3<String, Integer, PortableDataStream> out = new Tuple3<>(in._1(), index, in._2());
        String newKey = converter.getKey(in._1());
        return new Tuple2<>(newKey, out);
    }
}
