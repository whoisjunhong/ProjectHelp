//
//  VolunteerScheduledTableViewCell.swift
//  Project Help
//
//  Created by student on 5/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit

class VolunteerScheduledTableViewCell: UITableViewCell {

    @IBOutlet var lblRequestType: UILabel!
    
    @IBOutlet var lblAddress: UILabel!
    @IBOutlet var lblName: UILabel!
    @IBOutlet var lblStatus: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
