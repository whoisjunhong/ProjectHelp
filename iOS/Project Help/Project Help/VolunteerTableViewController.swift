//
//  VolunteerTableViewController.swift
//  Project Help
//
//  Created by student on 6/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class VolunteerTableViewController: UITableViewController {
    
    @IBOutlet var scheduledTableView: UITableView!
    
    var refreshData : UIRefreshControl!
    
    var request = Array<Elderly>()
    
    var status:String! = ""
    
    var getRequestUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/getRequest.php"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        refreshData = UIRefreshControl()
        refreshData.attributedTitle = NSAttributedString(string: "Pull to refresh schedule")
        refreshData?.addTarget(self, action: #selector(ScheduledViewController.refreshSchedule(_ :)), forControlEvents: UIControlEvents.ValueChanged)
        scheduledTableView.backgroundView = refreshData
        self.refreshData.superview?.sendSubviewToBack(self.refreshData)
    }
    
    func refreshSchedule(sender: AnyObject) {
        request.removeAll()
        getRequest()
        self.scheduledTableView.reloadData()
        self.refreshData?.endRefreshing()
    }
    override func viewDidAppear(animated: Bool) {
        getRequest()
    }
    
    override func viewDidDisappear(animated: Bool) {
        request.removeAll()
    }
    
    func getRequest() {
        let parameters: [String: AnyObject] = [
            "msgType" : GlobalVariable.GET_REQUEST
        ]
        
        Alamofire.request(.POST, getRequestUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "OK" {
                let elderlyObject = JSON(jsonResponse["requestDetails"].array!)
                
                for (_, object) in elderlyObject {
                    let requestId = object["requestId"].intValue
                    let requestee = object["requestee"].stringValue
                    let requestType = object["type"].stringValue
                    let requestGender = object["gender"].stringValue
                    let requestAddress = object["address"].stringValue
                    let unitno = object["unitno"].stringValue
                    let locationLat = object["locationLat"].doubleValue
                    let locationLong = object["locationLong"].doubleValue
                    let requestDate = object["requestDate"].stringValue
                    let requestTime = object["requestTime"].stringValue
                    let requestStatus = object["requestStatus"].stringValue
                    
                    print(requestId)
                    print(requestee)
                    print(requestType)
                    print(requestGender)
                    print(requestAddress)
                    print(unitno)
                    print(locationLat)
                    print(locationLong)
                    print(requestDate)
                    print(requestTime)
                    print(requestStatus)
                    
                    let myElderly = Elderly(requestId: requestId, name: requestee, gender: requestGender, type: requestType,   address: requestAddress, unitno: unitno, locationLat: locationLat, locationLong: locationLong, date: requestDate, time: requestTime, status: requestStatus)
                    
                    self.request.append(myElderly)
                }
                self.scheduledTableView.reloadData()
            }
                
            else {
                self.toastBox("Error retrieving schedule!")
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return request.count
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = "cell"
        
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! VolunteerTableViewCell
        
        // Configure the cell
        
        cell.lblName?.text = self.request[indexPath.row].name
        cell.lblAddress?.text = self.request[indexPath.row].address
        cell.lblRequestType?.text = self.request[indexPath.row].type
        if self.request[indexPath.row].status == "T" {
            cell.lblStatus?.text = self.request[indexPath.row].name + " needs help!"
            cell.lblStatus.textColor = UIColor.orangeColor()
        }
        
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        print("You selected cell #\(indexPath.row)!")
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showRequestDetail" {
            if let indexPath = tableView.indexPathForSelectedRow {
                
                let destinationController = segue.destinationViewController as! VolunteerHelpDetailViewController
                destinationController.id = String(self.request[indexPath.row].requestId)
                destinationController.name = self.request[indexPath.row].name
                destinationController.type = self.request[indexPath.row].type
                destinationController.gender = self.request[indexPath.row].gender
                destinationController.address = self.request[indexPath.row].address
                destinationController.unitno = self.request[indexPath.row].unitno
                destinationController.date = self.request[indexPath.row].date
                destinationController.time = self.request[indexPath.row].time
                destinationController.locationLat = String(self.request[indexPath.row].locationLat)
                destinationController.locationLong = String(self.request[indexPath.row].locationLong)
                destinationController.status = String(self.request[indexPath.row].status)
            }
        }
    }
}
