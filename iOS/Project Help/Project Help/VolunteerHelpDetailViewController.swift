//
//  VolunteerHelpDetailedViewController.swift
//  Project Help
//
//  Created by student on 7/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class VolunteerHelpDetailViewController: UIViewController {

    let defaults = NSUserDefaults.standardUserDefaults()
    
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
    
    var acceptRequestUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/apply.php"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lblAddress.titleLabel!.lineBreakMode = .ByWordWrapping
        lblAddress.titleLabel!.numberOfLines = 4
        lblAddress.titleLabel!.textAlignment = .Left
        
        lblElderly?.text = name
        lblRequestType?.text = type
        lblGender?.text = gender
        lblAddress.setTitle(address, forState: .Normal)
        lblUnitNo?.text = unitno
        lblDate?.text = date
        lblTime?.text = time
        
    }
    
    override func viewDidAppear(animated: Bool) {
        if status == "T" {
            button.setTitle("Help Elderly", forState: .Normal)
        }
        else if status == "P" {
            button.setTitle("Request in progress", forState: .Disabled)
            button.enabled = false
            button.backgroundColor = UIColor.lightGrayColor()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func btnHelp(sender: AnyObject) {
        let parameters: [String: AnyObject] = [
            "aceptee" : self.defaults.valueForKey("id")!,
            "requestId" : self.id,
            "msgType" : GlobalVariable.GET_REQUEST
        ]
        
        Alamofire.request(.POST, acceptRequestUrl, parameters:parameters, headers: nil).validate().responseJSON {
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
                self.toastBox("Unable to accept request!")
            }
        }
    }
    
    @IBAction func btnAddress(sender: AnyObject) {
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"comgooglemaps://")!)) {
            UIApplication.sharedApplication().openURL(NSURL(string: "comgooglemaps://?api=1&q=" + locationLat + "," + locationLong + "")!)
        } else {
            print("Can't use comgooglemaps://");
        }
    }
}
