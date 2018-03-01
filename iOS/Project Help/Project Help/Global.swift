import UIKit

struct GlobalVariable {
    //static var HOST = "127.0.0.1"
    //static var HOST = "10.0.2.2"
    //static var HOST = "172.20.10.2"
    //static var HOST = "172.27.140.95"
    static var HOST = "192.168.1.106"
    //static var HOST = "isg’s%20MacBook%20Pro.local"
    
    static var DIR = "projecthelp/iOS"
    
    static var LOGIN_REQUEST = 1
    static var CHECKLOGIN = 2
    static var REGISTER_REQUEST = 3
    static var UPDATE_REQUEST = 4
    static var GET_USER = 5
    static var REQUEST_HELP = 6
    static var GET_REQUEST = 7
    static var GET_VOLUNTEER_REQUEST = 8
    static var GET_ELDERLY_REQUEST = 9
    static var DELETE_REQUEST = 10
}

extension UIViewController {
    
    func toastBox(message : String) {
        
        let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 150, y: self.view.frame.size.height-100, width: 300, height: 35))
        toastLabel.backgroundColor = UIColor.blackColor().colorWithAlphaComponent(0.6)
        toastLabel.textColor = UIColor.whiteColor()
        toastLabel.textAlignment = .Center;
        toastLabel.font = UIFont(name: "Montserrat-Light", size: 12.0)
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        self.view.addSubview(toastLabel)
        UIView.animateWithDuration(4.0, delay: 0.1, options: .CurveEaseOut, animations: {
            toastLabel.alpha = 0.0
            }, completion: {(isCompleted) in
                toastLabel.removeFromSuperview()
        })
    } }
