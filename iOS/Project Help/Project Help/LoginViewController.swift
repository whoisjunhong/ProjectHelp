//
//  LoginViewController.swift
//  Project Help
//
//  Created by student on 24/1/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class LoginViewController: UIViewController {

    @IBOutlet var tbUsername: UITextField!
    @IBOutlet var tbPassword: UITextField!
    
    var username:String! = ""
    var password:String! = ""
    
    var status:String! = ""
    var userType:String! = ""
    
    var loginUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/loginUser.php"
    var checkLoginUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/checkLogin.php"
    
    let defaults = NSUserDefaults.standardUserDefaults()
    
    @IBAction func btnLogin(sender: AnyObject) {
        username = tbUsername.text
        password = tbPassword.text
        
        if username == "" || password == "" {
            toastBox("All fields are required!")
        }
        else {
            loginUser()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if self.defaults.valueForKey("id") != nil {
            checkLogin()
        }
    }
    
    private func loginUser(){
        
        // "Like convert to json"
        let parameters: [String: AnyObject] = [
            "username" : self.username,
            "password" : self.password,
            "msgType" : GlobalVariable.LOGIN_REQUEST
        ]
        
        Alamofire.request(.POST, loginUrl, parameters:parameters, headers: nil).validate().responseJSON {
            (responseObject) in
            //print(responseObject)
            
            //Get JSON resposne
            let jsonResponse = JSON(responseObject.2.value!)
            print(jsonResponse)

            // Converts object to string
            self.status = jsonResponse["status"].string!
            if self.status == "OK" {
                let id = jsonResponse["id"].string!
                let checkLogin = jsonResponse["checkLogin"].string!
                let userType = jsonResponse["usertype"].string!
                
                self.defaults.setObject(id, forKey: "id")
                self.defaults.setObject(checkLogin, forKey: "checkLogin")
                
                if userType == "Elderly" {
                    self.performSegueWithIdentifier("elderlyLoginIdentifier", sender: self)
                }
                else if userType == "Volunteer" {
                    self.performSegueWithIdentifier("volunteerLoginIdentifier", sender: self)
                }
                else {
                    for key in self.defaults.dictionaryRepresentation().keys {
                        self.defaults.removeObjectForKey(key)
                    }
                    self.toastBox("Username or Password is incorrect!")
                }
            }
            else {
                self.toastBox("Username or Password is incorrect!")

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
            
            print(self.defaults.valueForKey("id"))
            print(self.defaults.valueForKey("id"))
        
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
                        self.userType = jsonResponse["usertype"].string!
                            if self.userType == "Elderly" {
                                self.performSegueWithIdentifier("elderlyLoginIdentifier", sender: self)
                            }
                            else if self.userType == "Volunteer" {
                                self.performSegueWithIdentifier("volunteerLoginIdentifier", sender: self)
                            }
                    }
                    else {
                        for key in self.defaults.dictionaryRepresentation().keys {
                        self.defaults.removeObjectForKey(key)
                    }
                    self.toastBox("Multiple logins detected! Login to retry")
                    }
                }
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
    
    override func viewWillAppear(animated: Bool) {
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
        super.viewWillAppear(animated)
    }
    
    override func viewWillDisappear(animated: Bool) {
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
        super.viewWillDisappear(animated)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
    }

    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {
        
        if let iden = identifier {
            if iden == "toLogin" || iden == "toRegister" {
                return true
            }
        }
        return false
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
