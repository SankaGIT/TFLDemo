/*
 * Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.sample.tfl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class TflStream {
	static HashMap<String, BusStop> map = new HashMap<String, BusStop>();
	static ConcurrentHashMap<String, Bus> busses = new ConcurrentHashMap<String, Bus>();
	public static long timeOffset;
	public static long lastTime = 0;
	public static final String endPointBus =  "http://localhost:9763/endpoints/GpsDataOverHttp/trackingstream";
	public static final String endPointTraffic =  "http://localhost:9763/endpoints/GpsDataOverHttp/geostream";

	public static void main(String[] args) throws XMLStreamException {
		//Disruption disruption = new Disruption("id123", "severity123", "locationThis", "comment");
		//disruption.addCoordsLane("0,0,1,1");
		//disruption.setCoordsPoly("0,2,3,4");
		//System.out.println(disruption.toString());
		try {
			Update update = new Update(System.currentTimeMillis(), 1000, endPointBus);
			GetData g = new GetData();
			g.start();
			System.out.println("Started getting data");
			Thread.sleep(30000);
			update.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void send(ArrayList<String> jsonList, String endPoint) {

		for (String data : jsonList) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(endPoint);
			try {

				StringEntity entity = new StringEntity(data);
				post.setEntity(entity);
				//System.out.println("The Message Sent : " + data);
				HttpResponse response = client.execute(post);
				// System.out.println(response);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
