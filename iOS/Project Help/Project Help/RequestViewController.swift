//
//  RequestViewController.swift
//  Project Help
//
//  Created by student on 4/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import LocationPicker
import CoreLocation
import MapKit

class RequestViewController: UIViewController {

    let defaults = NSUserDefaults.standardUserDefaults()
    
    let locationPicker = LocationPickerViewController()
    
    @IBAction func btnSelectLocation(sender: AnyObject) {
    }
    @IBOutlet var lblName: UILabel!
    @IBOutlet var lblGender: UILabel!
    @IBOutlet var lblUserType: UILabel!
    @IBOutlet var pvRequestType: UIPickerView!
    @IBOutlet var tbAddress: UITextField!
    @IBOutlet var tbUnitNo: UITextField!
    @IBOutlet var tbDate: UIDatePicker!
    @IBOutlet var tbTime: UIDatePicker!
    @IBOutlet var btnLocationOutlet: UIButton!
    
    var addressDefault : String!
    
    var name:String! = ""
    var gender:String! = ""
    var userType:String! = ""
    var address:String! = ""
    var unitNo:String! = ""
    var date:NSDate!
    var time:NSDate!
    var status:String! = ""
    
    var getUserUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/getUser.php"
    var requestServiceURL:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/requestService.php"
    
    var requestTypeData : [String] = [String]()
    var requestType:String! = "Clean House"
    
    var location: Location? {
        didSet {
            tbAddress.text = location.flatMap({ $0.title }) ?? "No location selected"
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getLatestAddress()
        requestTypeData = ["Clean House", "Do Laundry", "Cook Meals", "Accompany Me"]
        tbDate.setValue(UIColor.whiteColor(), forKeyPath: "textColor")
        tbTime.setValue(UIColor.whiteColor(), forKeyPath: "textColor")
        locationPicker.showCurrentLocationButton = true
        
    }
    
    override func viewDidAppear(animated: Bool) {
        getUser()
        self.navigationController?.navigationBarHidden = true
        btnLocationOutlet.setTitleColor(UIColor(red: 2/255.0, green: 110/255.0, blue: 202/255.0, alpha: 1.0), forState: .Normal)
    }
    
    @IBAction func btnRequest(sender: AnyObject) {
        
        name = lblName.text
        gender = lblGender.text
        address = tbAddress.text
        unitNo = tbUnitNo.text
        date = tbDate.date
        time = tbTime.date
        
        if address == "" || unitNo == "" {
            toastBox("All fields are required!")
        }
        else if tbAddress.text == addressDefault {
            toastBox("Please confirm your address again!")
            btnLocationOutlet.setTitleColor(.redColor(), forState: .Normal)
        }
        else {
            sendRequest()
        }
    }
    
    func sendRequest() {
        // "Like convert to json"
        let parameters: [String: AnyObject] = [
            "id" : self.defaults.valueForKey("id")!,
            "name" : self.name,
            "gender" : self.gender,
            "address" : self.address,
            "unitno" : self.unitNo,
            "servicetype" : self.requestType,
            "date" : self.date,
            "time" : self.time,
            "lat" : (self.location?.coordinate.latitude)!,
            "long" : (self.location?.coordinate.longitude)!,
            "msgType" : GlobalVariable.REQUEST_HELP
        ]
        
        Alamofire.request(.POST, requestServiceURL, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "OK" {
                self.toastBox("Request Success!!")
                self.tabBarController?.selectedIndex = 2
            }
            else {
                self.toastBox("Unable to register!")
            }
        }
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "LocationPicker" {
            let locationPicker = segue.destinationViewController as! LocationPickerViewController
            locationPicker.location = location
            locationPicker.showCurrentLocationButton = true
            locationPicker.useCurrentLocationAsHint = true
            locationPicker.showCurrentLocationInitially = true
            
            locationPicker.completion = { self.location = $0 }
        }
    }
    
    func getUser() {
        let parameters: [String: AnyObject] = [
            "id" : self.defaults.valueForKey("id")!,
            "msgType" : GlobalVariable.GET_USER
        ]
        
        Alamofire.request(.POST, getUserUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "SelectSuccess" {
                let nameParse = jsonResponse["name"].string!
                let genderParse = jsonResponse["gender"].string!
                let userTypeParse = jsonResponse["usertype"].string!
                let unitNoParse = jsonResponse["unitno"].string!

                self.tbUnitNo.text = unitNoParse
                self.lblGender.text = genderParse
                self.lblUserType.text = userTypeParse
                self.lblName.text = nameParse
                
            }
            else {
                self.toastBox("Error retrieving profile!")
            }
        }
    }
    
    func getLatestAddress() {
        let parameters: [String: AnyObject] = [
            "id" : self.defaults.valueForKey("id")!,
            "msgType" : GlobalVariable.GET_USER
        ]
        
        Alamofire.request(.POST, getUserUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "SelectSuccess" {
                let addressParse = jsonResponse["address"].string!
                
                self.addressDefault = addressParse
                self.tbAddress.text = addressParse
            }
            else {
                self.toastBox("Error retrieving profile!")
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
        // Dispose of any resources that can be recreated.
    }
    
    // The number of columns of data
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // The number of rows of data
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return requestTypeData.count
    }
    
    // The data to return for the row and component (column) that's being passed in
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {

        return requestTypeData[row]
    }
    
    func pickerView(pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        let str = requestTypeData[row]
 
        return NSAttributedString(string: str, attributes: [NSForegroundColorAttributeName:UIColor.whiteColor()])
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        self.requestType = requestTypeData[row]
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
