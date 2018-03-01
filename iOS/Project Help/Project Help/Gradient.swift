//
//  File.swift
//  Project Help
//
//  Created by student on 24/1/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import Foundation
import UIKit

class GradientBlue: UIView {
    override class func layerClass() -> AnyClass { return CAGradientLayer.self }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        let gradientLayer = self.layer as! CAGradientLayer
        gradientLayer.colors = [UIColor(red: 112/255.0, green: 185/255.0, blue: 245/255.0, alpha: 1.0).CGColor, UIColor(red: 52/255.0, green: 136/255.0, blue: 209/255.0, alpha: 1.0).CGColor, UIColor(red: 2/255.0, green: 110/255.0, blue: 202/255.0, alpha: 1.0).CGColor]
    }
}

class GradientGreen: UIView {
    override class func layerClass() -> AnyClass { return CAGradientLayer.self }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        let gradientLayer = self.layer as! CAGradientLayer
        gradientLayer.colors = [UIColor(red: 171/255.0, green: 234/255.0, blue: 171/255.0, alpha: 1.0).CGColor, UIColor(red: 110/255.0, green: 204/255.0, blue: 110/255.0, alpha: 1.0).CGColor, UIColor(red: 90/255.0, green: 196/255.0, blue: 90/255.0, alpha: 1.0).CGColor]
    }
}