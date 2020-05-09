set foreign_key_checks=0;
-- delete supplier
delete p, po_c, po, po_i, pa, um
from processes as p
		left join 
    po_codes as po_c on p.po_code_code = po_c.code
		left join 
	purchase_orders as po on po.process_id = p.id
		left join 
	po_items as po_i on po_i.poid = po.process_id
		left join 
	process_approvals as pa on pa.process_id = p.id
		left join
	user_messages as um on um.process_id = p.id	
where
	p.id in ();
    

