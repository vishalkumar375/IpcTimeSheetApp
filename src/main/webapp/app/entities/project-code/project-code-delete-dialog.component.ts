import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from './project-code.service';

@Component({
    selector: 'jhi-project-code-delete-dialog',
    templateUrl: './project-code-delete-dialog.component.html'
})
export class ProjectCodeDeleteDialogComponent {
    projectCode: IProjectCode;

    constructor(
        private projectCodeService: ProjectCodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectCodeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'projectCodeListModification',
                content: 'Deleted an projectCode'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-code-delete-popup',
    template: ''
})
export class ProjectCodeDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectCode }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProjectCodeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.projectCode = projectCode;
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
