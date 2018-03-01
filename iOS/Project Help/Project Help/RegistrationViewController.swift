//
//  RegistrationViewController.swift
//  Project Help
//
//  Created by student on 24/1/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class RegistrationViewController: UIViewController {


    @IBOutlet var tbName: UITextField!
    @IBOutlet var tbUsername: UITextField!
    @IBOutlet var tbPassword: UITextField!
    @IBOutlet var pvGender: UIPickerView!
    @IBOutlet var tbAddress: UITextField!
    @IBOutlet var tbUnitNo: UITextField!
    @IBOutlet var pvUserType: UIPickerView!
    
    var name:String! = ""
    var username:String! = ""
    var password:String! = ""
    var gender:String! = "Male"
    var address:String! = ""
    var unitno:String! = ""
    var usertype:String! = "Elderly"
    
    var status:String! = ""
    
    var registerUserUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/registerUser.php"
    var checkLoginUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/checkLogin.php"
    
    var genderData : [String] = [String]();
    var userTypeData : [String] = [String]();
    
    let defaults = NSUserDefaults.standardUserDefaults()
    
    @IBAction func btnRegister(sender: AnyObject) {
        name = tbName.text
        username = tbUsername.text
        password = tbPassword.text
        address = tbAddress.text
        unitno = tbUnitNo.text
        if name == "" || username == ""  || password == "" || address == "" || unitno == "" {
            toastBox("All fields are required!")
        }
        else if username == " " {
            toastBox("Username cannot have spaces!")
        }
        else {
            registerUser()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.defaults.valueForKey("id") != nil {
            checkLogin()
        }
        genderData = ["Male", "Female"]
        userTypeData = ["Elderly", "Volunteer"]
    }
    
    private func registerUser() {
        
        // "Like convert to json"
        let parameters: [String: AnyObject] = [
            "name" : self.name,
            "username" : self.username,
            "password" : self.password,
            "gender" : self.gender,
            "address" : self.address,
            "unitno" : self.unitno,
            "usertype" : self.usertype,
            "msgType" : GlobalVariable.REGISTER_REQUEST
        ]
        
        print(self.name)
        print(self.username)
        print(self.password)
        print(self.gender)
        print(self.address)
        print(self.usertype)
        print(self.unitno)
        Alamofire.request(.POST, registerUserUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "OK" {
                let id = jsonResponse["id"].int!
                let checkLogin = jsonResponse["checkLogin"].string!
                
                self.defaults.setObject(id, forKey: "id")
                self.defaults.setObject(checkLogin, forKey: "checkLogin")
                
                if self.usertype == "Elderly" {
                    self.performSegueWithIdentifier("elderlyRegisterIdentifier", sender: self)
                }
                else if self.usertype == "Volunteer" {
                    self.performSegueWithIdentifier("volunteerRegisterIdentifier", sender: self)
                }
                else {
                    self.toastBox("Unable to register!")
                }
            }
            else {
                self.toastBox("Unable to register!")
            }
        }
    }
    
    private func checkLogin() {
        // Set post values
        if self.defaults.valueForKey("id") == nil || self.defaults.valueForKey("checkLogin") == nil {
            // Show error mesaage on failed verification
        }
        else {
            let params:[String: AnyObject] = [
                "id" : self.defaults.valueForKey("id")!,
                "checkLogin" : self.defaults.valueForKey("checkLogin")!,
                "msgType" : GlobalVariable.CHECKLOGIN
            ]
            
            // Send request
            Alamofire.request(.POST, checkLoginUrl, parameters:params, headers:nil).validate().responseJSON {
                (responseObject) in
                // Receive Object
                print(responseObject)
                let jsonResponse = JSON(responseObject.2.value!)
                print(jsonResponse)
                
                // Convert Object to String
                let msgType = jsonResponse["msgType"].string!
                if msgType == String(GlobalVariable.CHECKLOGIN) {
                    self.status = jsonResponse["status"].string!
                    if self.status == "OK" {
                        //let currentUser = jsonResponse["username"].string!
                        
                        self.performSegueWithIdentifier("elderlyLoginIdentifier", sender: nil)
                        // output welcome message here
                    }
                    else {
                        for key in self.defaults.dictionaryRepresentation().keys {
                            self.defaults.removeObjectForKey(key)
                        }
                        // output error message here
                    }
                }
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
        
        if (pickerView.tag == 1) {
            return genderData.count
        }
        else {
            return userTypeData.count
        }
    }
    
    // The data to return for the row and component (column) that's being passed in
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if (pickerView.tag == 1) {
            return "\(genderData[row])"
        }
        else {
            return "\(userTypeData[row])"
        }
    }
    
    // When user touches outside the keyboard, it hides it
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func pickerView(pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        let str = genderData[row]
        let str1 = userTypeData[row]
        
        if (pickerView.tag == 1) {
            return NSAttributedString(string: str, attributes: [NSForegroundColorAttributeName:UIColor.whiteColor()])
        }
        else {
            return NSAttributedString(string: str1, attributes: [NSForegroundColorAttributeName:UIColor.whiteColor()])
        }
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        if pickerView.tag == 1 {
            self.gender = genderData[row]
        }
        else {
            self.usertype = userTypeData[row]
        }
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
