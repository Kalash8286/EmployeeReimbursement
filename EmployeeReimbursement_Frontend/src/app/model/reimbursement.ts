import { ReimbursementType } from "./reimbursement-type";

export class Reimbursement {
    id!: number;
    travelRequestId!: number;
    requestRaisedByEmployeeId!: number;
    requestDate!: Date;
    reimbursementType!: ReimbursementType;
    invoiceNo!: string;
    invoiceDate!: Date;
    invoiceAmount!: number;
    documentUrl!: string;
    requestProcessedOn!: Date;
    requestProcessedByEmployeeId!: number;
    status!: string;
    remark!: string;
}
