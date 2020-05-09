-- delete supplier
delete c, s, sc, cc, pr, idc, cd, ph, f, e, ad, pa, bp, pcd, pph, pf, pe, pad, ppa, pbp 
from companies as c
		left join 
    suppliers as s on c.id = s.company_id
			left join 
		suppliers_categories as sc on sc.company_id = s.company_id
		left join 
    company_contacts as cc on c.id = cc.company_id
			left join 
		persons as pr on pr.id = cc.person_id
				left join 
			id_information as idc on pr.id = idc.person_id	
				left join 
			contact_details as pcd on pr.id = pcd.person_id
					left join 
				phones as pph on pcd.id = pph.contact_id
					left join 
				faxes as pf on pcd.id = pf.contact_id
					left join 
				emails as pe on pcd.id = pe.contact_id
					left join 
				addresses as pad on pcd.id = pad.contact_id
					left join 
				payment_accounts as ppa on pcd.id = ppa.contact_id
						left join 
							bank_payees as pbp on ppa.id = pbp.payment_id
		left join 
    contact_details as cd on c.id = cd.company_id
			left join 
		phones as ph on cd.id = ph.contact_id
			left join 
		faxes as f on cd.id = f.contact_id
			left join 
		emails as e on cd.id = e.contact_id
			left join 
		addresses as ad on cd.id = ad.contact_id
			left join 
		payment_accounts as pa on cd.id = pa.contact_id
				left join 
					bank_payees as bp on pa.id = bp.payment_id
where
	c.id in (7);
    

