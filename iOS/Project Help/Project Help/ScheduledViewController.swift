//
//  ScheduledViewController.swift
//  Project Help
//
//  Created by student on 5/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ScheduledViewController: UITableViewController {
    
    let defaults = NSUserDefaults.standardUserDefaults()
    
    var refreshData : UIRefreshControl!

    @IBOutlet var scheduledTableView: UITableView!
    
    var request = Array<Elderly>()
    
    var status:String! = ""
    
    var getElderlySchedule:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/getElderlySchedule.php"
    var deleteRequestUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/deleteRequest.php"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
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
        request.removeAll()
        let parameters: [String: AnyObject] = [
            "id" : self.defaults.valueForKey("id")!,
            "msgType" : GlobalVariable.GET_ELDERLY_REQUEST
        ]
        
        Alamofire.request(.POST, getElderlySchedule, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "OK" {
                let elderlyObject = JSON(jsonResponse["elderlyScheduledDetails"].array!)
                
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
    
    override func tableView(tableView: UITableView, editActionsForRowAtIndexPath
        indexPath: NSIndexPath) -> [UITableViewRowAction]? {
        // Delete button
        let deleteAction = UITableViewRowAction(style:
            UITableViewRowActionStyle.Default, title: "Delete",handler: { (action,
                indexPath) -> Void in
                // "Like convert to json"
                let parameters: [String: AnyObject] = [
                    "requestId" : self.request[indexPath.row].requestId,
                    "msgType" : GlobalVariable.DELETE_REQUEST
                ]
                
                Alamofire.request(.POST, self.deleteRequestUrl, parameters:parameters, headers: nil).validate().responseJSON {
                    (responseObject) in
                    print(responseObject)
                    
                    //Get JSON resposne
                    let jsonResponse = JSON(responseObject.2.value!)
                    print(jsonResponse)
                    
                    // Converts object to string
                    self.status = jsonResponse["status"].string!
                    if self.status == "OK" {
                        self.navigationController?.popViewControllerAnimated(true)
                        self.toastBox("Request deleted!")
                        self.getRequest()
                        
                    }
                    else {
                        self.toastBox("Unable to delete request!")
                    }
                }
        })
        return [deleteAction]
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = "cell"
        
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! ScheduledCellTableViewCell
        
        // Configure the cell
        
        cell.lblName?.text = self.request[indexPath.row].name
        cell.lblAddress?.text = self.request[indexPath.row].address
        cell.lblType?.text = self.request[indexPath.row].type
        if self.request[indexPath.row].status == "T" {
            cell.lblStatus?.text = "Waiting for volunteer to respond!"
            cell.lblStatus.textColor = UIColor.orangeColor()
        }
        else if self.request[indexPath.row].status == "P" {
            cell.lblStatus?.text = "Volunteer found! He/She will come at " + self.request[indexPath.row].time + " on " + self.request[indexPath.row].date
            cell.lblStatus.textColor = UIColor.greenColor()
        }
        else if self.request[indexPath.row].status == "F" {
            cell.lblStatus?.text = "Request Completed!"
            cell.lblStatus.textColor = UIColor.lightGrayColor()
        }
        //cell.thumbnailImageView?.image = UIImage(named: restaurants[indexPath.row].image)
        
        return cell
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showRequestDetail" {
            if let indexPath = tableView.indexPathForSelectedRow {
                
                let destinationController = segue.destinationViewController as! ScheduleDetailViewController
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
