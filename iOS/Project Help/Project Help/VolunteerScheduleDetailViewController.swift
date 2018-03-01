//
//  VolunteerScheduleDetailViewController.swift
//  Project Help
//
//  Created by student on 7/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit

class VolunteerScheduleDetailViewController: UIViewController {

    let defaults = NSUserDefaults.standardUserDefaults()
    
    @IBOutlet var lblElderly: UILabel!
    @IBOutlet var lblRequestType: UILabel!
    @IBOutlet var lblGender: UILabel!
    @IBOutlet var lblUnitNo: UILabel!
    @IBOutlet var lblDate: UILabel!
    @IBOutlet var lblTime: UILabel!
    @IBOutlet var lblAddress: UIButton!
    
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
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func btnAddress(sender: AnyObject) {
        if (UIApplication.sharedApplication().canOpenURL(NSURL(string:"comgooglemaps://")!)) {
            UIApplication.sharedApplication().openURL(NSURL(string: "comgooglemaps://?api=1&q=" + locationLat + "," + locationLong + "")!)
        } else {
            print("Can't use comgooglemaps://");
        }
    }
}
