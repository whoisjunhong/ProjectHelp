//
//  ProfileViewController.swift
//  Project Help
//
//  Created by student on 31/1/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ProfileViewController: UIViewController {

    let defaults = NSUserDefaults.standardUserDefaults()
    
    @IBOutlet var tgrProfile: UITapGestureRecognizer!
    
    @IBOutlet var tbUsername: UITextField!
    @IBOutlet var lblGender: UILabel!
    @IBOutlet var lblUserType: UILabel!
    @IBOutlet var tbAddress: UITextField!
    @IBOutlet var tbUnitNo: UITextField!
    @IBOutlet var tbOldPassword: UITextField!
    @IBOutlet var tbNewPassword: UITextField!
    
    var username:String! = ""
    var gender:String! = ""
    var userType:String! = ""
    var address:String! = ""
    var unitNo:String! = ""
    var oldPassword:String! = ""
    var newPassword:String! = ""
    
    var status:String! = ""
    
    var getUserUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/getUser.php"
    
    var updateUserUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/updateProfile.php"
    
    @IBAction func btnSaveChanges(sender: AnyObject) {
        username = tbUsername.text
        address = tbAddress.text
        unitNo = tbUnitNo.text
        oldPassword = tbOldPassword.text
        newPassword = tbNewPassword.text
        
        if username == "" || address == "" || unitNo == "" || oldPassword == "" || newPassword == "" {
            toastBox("All fields are required!")
        }
        else if username == " " {
            toastBox("Username cannot have spaces!")
        }
        else {
            updateUser()
        }
        
    }
    
    func updateUser() {
        // "Like convert to json"
        let parameters: [String: AnyObject] = [
            "id" : self.defaults.valueForKey("id")!,
            "username" : self.username,
            "oldPassword" : self.oldPassword,
            "newPassword" : self.newPassword,
            "address" : self.address,
            "unitNo" : self.unitNo,
            "msgType" : GlobalVariable.UPDATE_REQUEST
        ]
        
        Alamofire.request(.POST, updateUserUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)
            
            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "UpdateSuccess" {
                self.toastBox("Profile Edited!")
            }
            else if self.status == "WrongPassword" {
                self.toastBox("Current password incorrect!")
            }
            else {
                self.toastBox("Unable to register!")
            }
        }

    }
    
    @IBAction func btnLogout(sender: AnyObject) {
        for key in self.defaults.dictionaryRepresentation().keys {
            self.defaults.removeObjectForKey(key)
        }
        self.performSegueWithIdentifier("logoutIdentifier", sender: self)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getUser()
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
                let usernameParse = jsonResponse["username"].string!
                let genderParse = jsonResponse["gender"].string!
                let userTypeParse = jsonResponse["usertype"].string!
                let addressParse = jsonResponse["address"].string!
                let unitNoParse = jsonResponse["unitno"].string!
                
                self.tbAddress.text = addressParse
                self.tbUnitNo.text = unitNoParse
                self.lblGender.text = genderParse
                self.lblUserType.text = userTypeParse
                self.tbUsername.text = usernameParse
                

                // Set text here
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
    
    // When user touches outside the keyboard, it hides it
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true) //This will hide the keyboard
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
