//
//  Elderly.swift
//  Project Help
//
//  Created by student on 5/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import CoreLocation

struct Elderly {
    
    var requestId:Int!
    var name:String!
    var gender:String!
    var type:String!
    var address:String!
    var unitno:String!
    var locationLat:Double!
    var locationLong:Double!
    var date:String!
    var time:String!
    var status:String!

    init(requestId:Int, name:String, gender:String, type:String, address:String, unitno:String, locationLat:Double, locationLong:Double, date:String, time:String, status:String) {
        
        self.requestId = requestId
        self.name = name
        self.gender = gender
        self.type = type
        self.address = address
        self.unitno = unitno
        self.locationLat = locationLat
        self.locationLong = locationLong
        self.date = date
        self.time = time
        self.status = status
    }
}
