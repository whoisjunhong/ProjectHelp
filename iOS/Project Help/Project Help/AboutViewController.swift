//
//  AboutViewController.swift
//  Project Help
//
//  Created by student on 4/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit

class AboutViewController: UIViewController {

    @IBAction func btnPhone(sender: AnyObject) {
        
        
        // Create an option menu as an actio sheet
        let optionMenu = UIAlertController(title: nil, message:"What do you want to do?", preferredStyle: .ActionSheet)
        
        // Add actions to the menu
        let cancelAction = UIAlertAction(title: "Cancel", style:.Cancel, handler: nil)
        optionMenu.addAction(cancelAction)
        // Display the menu
        self.presentViewController(optionMenu, animated: true, completion: nil)
        
        let callActionHandler = {
            (action:UIAlertAction) -> Void in
            UIApplication.sharedApplication().openURL(NSURL(string: "telprompt://64515115")!)
        }
        
        let callAction = UIAlertAction(title: "Call " + "+65 64515115", style: UIAlertActionStyle.Default, handler: callActionHandler)
        
        optionMenu.addAction(callAction)
        
    }
    
    @IBAction func btnEmail(sender: AnyObject) {
        // define email address
        let address = "weejunhong@gmail.com"
        
        let url = NSURL(string: "mailto:\(address)")!
        
        if UIApplication.sharedApplication().canOpenURL(url) {
            UIApplication.sharedApplication().openURL(url)
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
