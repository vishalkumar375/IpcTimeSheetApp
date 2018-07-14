import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { TimeSheetService } from './time-sheet.service';

@Component({
    selector: 'jhi-time-sheet-delete-dialog',
    templateUrl: './time-sheet-delete-dialog.component.html'
})
export class TimeSheetDeleteDialogComponent {
    timeSheet: ITimeSheet;

    constructor(private timeSheetService: TimeSheetService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timeSheetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'timeSheetListModification',
                content: 'Deleted an timeSheet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-time-sheet-delete-popup',
    template: ''
})
export class TimeSheetDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ timeSheet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TimeSheetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.timeSheet = timeSheet;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
