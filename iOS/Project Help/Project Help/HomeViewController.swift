//
//  HomeViewController.swift
//  Project Help
//
//  Created by student on 4/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class HomeViewController: UIViewController {

    @IBAction func btnPolice(sender: AnyObject) {
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"comgooglemaps://")!)) {
            UIApplication.sharedApplication().openURL(NSURL(string:
                "comgooglemaps://?q=Police+Station+Singapore&center=1.3521,103.8198")!)
        } else {
            print("Can't use comgooglemaps://");
        }
    }
    let defaults = NSUserDefaults.standardUserDefaults()
    var status:String! = ""
    @IBOutlet var lblWelcome: UILabel!

    @IBAction func btnAbout(sender: AnyObject) {
        self.tabBarController?.selectedIndex = 4
    }
    @IBAction func btnProfile(sender: AnyObject) {
        self.tabBarController?.selectedIndex = 3
    }
    var getUserUrl:String = "http://" + GlobalVariable.HOST + "/" + GlobalVariable.DIR + "/getUser.php"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tabBarController?.tabBar.hidden = false
        // Do any additional setup after loading the view.
        
        getUser()
    }
    
    override func viewDidAppear(animated: Bool) {
        self.navigationController?.navigationBarHidden = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

                self.lblWelcome.text = "Hi, " + nameParse + "!"
                
                
                // Set text here
            }
            else {
                self.toastBox("Error retrieving profile!")
            }
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
