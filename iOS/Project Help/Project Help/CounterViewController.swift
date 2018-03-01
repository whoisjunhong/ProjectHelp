//
//  CounterViewController.swift
//  Project Help
//
//  Created by student on 31/1/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit
import CoreMotion

class CounterViewController: UIViewController {
    
    @IBOutlet weak var activityState: UILabel!
    @IBOutlet weak var steps: UILabel!
    @IBOutlet var didyouknow: UILabel!
    
    var days:[String] = []
    var stepsTaken:[Int] = []
    
    @IBOutlet weak var stateImageView: UIImageView!
    let activityManager = CMMotionActivityManager()
    let pedoMeter = CMPedometer()
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        let cal = NSCalendar.currentCalendar()
        //var comps = cal.components(NSCalendarUnit.YearCalendarUnit | .MonthCalendarUnit | .DayCalendarUnit | .HourCalendarUnit | .MinuteCalendarUnit | .SecondCalendarUnit, fromDate: NSDate())
        
        //var comps1 = cal.comp
        let comps = cal.components([NSCalendarUnit.Year, NSCalendarUnit.Month, NSCalendarUnit.Day, NSCalendarUnit.Hour, NSCalendarUnit.Minute, NSCalendarUnit.Second], fromDate: NSDate())
        
        comps.hour = 0
        comps.minute = 0
        comps.second = 0
        let timeZone = NSTimeZone.systemTimeZone()
        cal.timeZone = timeZone
        
        let midnightOfToday = cal.dateFromComponents(comps)!
        
        
        if(CMMotionActivityManager.isActivityAvailable()) {
            self.activityManager.startActivityUpdatesToQueue(NSOperationQueue.mainQueue(), withHandler:
                {
                    (data : CMMotionActivity?) -> Void in
                    
                    dispatch_async(dispatch_get_main_queue(), { () -> Void in
                        if(data!.stationary == true){
                            self.activityState.text = "Stationary"
                            self.stateImageView.image = UIImage(named: "sitting")
                            self.didyouknow.text = "An average person walks 6,886 steps per day! That's 350 Calories which equates to running 4 kilometers!"
                        } else if (data!.walking == true){
                            self.activityState.text = "Walking"
                            self.didyouknow.text = "Regular, brisk walking can help prevent heart disease, high blood pressure and diabetes."
                            
                            self.stateImageView.image = UIImage(named: "walking")
                        } else if (data!.running == true){
                            self.activityState.text = "Running"
                            self.didyouknow.text = "Even if you’re a super-slow jogger, you’ll burn at least 10 calories per minute of running. That means you’ll run off half a block of Lindt Dark Chocolate in less than 23 min!"
                            self.stateImageView.image = UIImage(named: "running")
                        } else if (data!.automotive == true){
                            self.activityState.text = "Automotive"
                            self.stateImageView.image = UIImage(named: "F1")
                        }
                    })
                    
                }
                
                
            )
            
        }
        
        if(CMPedometer.isStepCountingAvailable()){
            let fromDate = NSDate(timeIntervalSinceNow: -86400 * 7)
            self.pedoMeter.queryPedometerDataFromDate(fromDate, toDate: NSDate()) { (data : CMPedometerData?, error) -> Void in
                print(data)
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    if(error == nil){
                        self.steps.text = "Steps: " + "\(data!.numberOfSteps)"
                    }
                })
                
            }
            
            self.pedoMeter.startPedometerUpdatesFromDate(midnightOfToday) { (data: CMPedometerData?, error) -> Void in
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    if(error == nil){
                        self.steps.text = "Steps: " + "\(data!.numberOfSteps)"
                    }
                })
            }
        }
    }
    
}