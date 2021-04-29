function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}

const CreateGoalFormModule = {
    el: {
        createGoalForm: document.getElementById('createGoalForm'),
        titleInput: document.querySelector('#createGoalForm #inputTitle'),
        titleHelpTextWrapper: document.querySelector('#createGoalForm #titleHelpTextWrapper'),
    },

    init: function () {
            // Abort if form element is not found
                     if (!this.el.createGoalForm) {
                         return;
                     }

            this.bindEventListeners()
        },

    bindEventListeners: function() {
        this.el.titleInput.addEventListener('keydown', this.onTitleKeydownHandler.bind(this))
    },

    onTitleKeydownHandler: function(event) {
        if (event.target.value.length > 130) {
            this.el.titleHelpTextWrapper.classList.remove('d-none');
        } else {
            this.el.titleHelpTextWrapper.classList.add('d-none');
        }
    },
}

const CreateSubgoalFormModule = {
    el: {
        createSubgoalForm: document.getElementById('createSubgoalForm'),
        titleInput: document.querySelector('#createSubgoalForm #inputTitle'),
        titleHelpTextWrapper: document.querySelector('#createSubgoalForm #titleHelpTextWrapper'),
    },

    init: function () {
        // Abort if form element is not found
        if (!this.el.createSubgoalForm) {
            return;
        }

        this.bindEventListeners()
    },

    bindEventListeners: function() {
        this.el.titleInput.addEventListener('keydown', this.onTitleKeydownHandler.bind(this))
    },

    onTitleKeydownHandler: function(event) {
        if (event.target.value.length > 130) {
            this.el.titleHelpTextWrapper.classList.remove('d-none');
        } else {
            this.el.titleHelpTextWrapper.classList.add('d-none');
        }
    },
}

docReady(function () {
    CreateGoalFormModule.init();
    CreateSubgoalFormModule.init();
})
