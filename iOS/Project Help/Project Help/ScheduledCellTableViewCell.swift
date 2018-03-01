//
//  ScheduledCellTableViewCell.swift
//  Project Help
//
//  Created by student on 5/2/18.
//  Copyright © 2018 studentSEG-DMIT. All rights reserved.
//

import UIKit

class ScheduledCellTableViewCell: UITableViewCell {

    @IBOutlet var lblName: UILabel!
    @IBOutlet var lblAddress: UILabel!
    @IBOutlet var lblStatus: UILabel!
    @IBOutlet var lblType: UILabel!
    @IBOutlet var userImage: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
