import { ReimbursementType } from "./reimbursement-type";

export class NewReimbursementRequest {

	id!: number;
	travelRequestId!: number;
	requestRaisedByEmployeeId!: number;
	requestDate!: Date;
	reimbursementTypeDTO!: ReimbursementType;
	reimbursementTypeId!:number;
	invoiceNo!: string;
	invoiceDate!: Date;
	invoiceAmount!: number;
	documentUrl!: string;
	requestProcessedOn!: Date;
	requestProcessedByEmployeeId!: number;
	status!: string;
	remark!: string;
	fromTravelDate!: Date;
	toTravelDate!: Date;

}
