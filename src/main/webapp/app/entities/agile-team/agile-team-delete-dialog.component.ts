import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAgileTeam } from 'app/shared/model/agile-team.model';
import { AgileTeamService } from './agile-team.service';

@Component({
    selector: 'jhi-agile-team-delete-dialog',
    templateUrl: './agile-team-delete-dialog.component.html'
})
export class AgileTeamDeleteDialogComponent {
    agileTeam: IAgileTeam;

    constructor(private agileTeamService: AgileTeamService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.agileTeamService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'agileTeamListModification',
                content: 'Deleted an agileTeam'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-agile-team-delete-popup',
    template: ''
})
export class AgileTeamDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agileTeam }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AgileTeamDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.agileTeam = agileTeam;
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
