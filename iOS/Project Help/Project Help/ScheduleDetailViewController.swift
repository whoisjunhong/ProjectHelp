//
//  ScheduleDetailViewController.swift
//  Project Help
//
//  Created by student on 5/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import CoreLocation

class ScheduleDetailViewController: UIViewController {

    @IBOutlet var lblElderly: UILabel!
    @IBOutlet var lblRequestType: UILabel!
    @IBOutlet var lblGender: UILabel!
    @IBOutlet var lblAddress: UIButton!
    @IBOutlet var lblUnitNo: UILabel!
    @IBOutlet var lblDate: UILabel!
    @IBOutlet var lblTime: UILabel!
    
    @IBOutlet var button: UIButton!

    var name = ""
    var type = ""
    var gender = ""
    var address = ""
    var unitno = ""
    var date = ""
    var time = ""
    var id = ""
    var locationLat = ""
    var locationLong = ""
    var status = ""
    
    var deleteRequestUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/deleteRequest.php"
    var markAsCompletedUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/markAsCompleted.php"

    
    override func viewDidLoad() {
        super.viewDidLoad()

        lblElderly?.text = name
        lblRequestType?.text = type
        lblGender?.text = gender
        lblAddress.setTitle(address, forState: .Normal)
        lblUnitNo?.text = unitno
        lblDate?.text = date
        lblTime?.text = time
        
        lblAddress.titleLabel!.lineBreakMode = .ByWordWrapping
        lblAddress.titleLabel!.numberOfLines = 4
        lblAddress.titleLabel!.textAlignment = .Left
        
        if status == "T" {
            button.setTitle("Delete Request", forState: .Normal)
        }
        else if status == "P" {
            button.setTitle("Mark as completed", forState: .Normal)
        }
        else if status == "F" {
            button.enabled = false
            button.setTitle("Request Completed", forState: .Normal)
            button.backgroundColor = UIColor.lightGrayColor()
        }
        
    }

    
    @IBAction func btnSubmit(sender: AnyObject) {
        switch status {
        case "T":
            // "Like convert to json"
            let parameters: [String: AnyObject] = [
                "requestId" : self.id,
                "msgType" : GlobalVariable.GET_REQUEST
            ]
            
            Alamofire.request(.POST, deleteRequestUrl, parameters:parameters, headers: nil).validate().responseJSON {
                (responseObject) in
                print(responseObject)
                
                //Get JSON resposne
                let jsonResponse = JSON(responseObject.2.value!)
                print(jsonResponse)
                
                // Converts object to string
                self.status = jsonResponse["status"].string!
                if self.status == "OK" {
                        self.navigationController?.popViewControllerAnimated(true)
                }
                else {
                    self.toastBox("Unable to delete request!")
                }
            }
            break
        case "P":
            // "Like convert to json"
            let parameters: [String: AnyObject] = [
                "requestId" : self.id,
                "msgType" : GlobalVariable.GET_REQUEST
            ]
            
            Alamofire.request(.POST, markAsCompletedUrl, parameters:parameters, headers: nil).validate().responseJSON {
                (responseObject) in
                print(responseObject)
                
                //Get JSON resposne
                let jsonResponse = JSON(responseObject.2.value!)
                print(jsonResponse)
                
                // Converts object to string
                self.status = jsonResponse["status"].string!
                if self.status == "OK" {
                    //self.performSegueWithIdentifier("backToScheduled", sender: self)
                    self.navigationController?.popViewControllerAnimated(true)
                }
                else {
                    self.toastBox("Unable to set as completed!")
                }
            }
            break
        default:
            print("Werk pls")
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    

    @IBAction func btnAddress(sender: AnyObject) {
        
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"comgooglemaps://")!)) {
        //UIApplication.sharedApplication().openURL(NSURL(string: "comgooglemaps://?api=1&q=" + address + "")!)
            UIApplication.sharedApplication().openURL(NSURL(string: "comgooglemaps://?api=1&q=" + locationLat + "," + locationLong + "")!)
        }
        else {
            print("Can't use comgooglemaps://");
        }
    }
}
